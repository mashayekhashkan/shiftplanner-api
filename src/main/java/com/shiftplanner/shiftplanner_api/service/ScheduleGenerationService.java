package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.*;
import com.shiftplanner.shiftplanner_api.repository.EmployeeBlockDayRepository;
import com.shiftplanner.shiftplanner_api.repository.EmployeeRepository;
import com.shiftplanner.shiftplanner_api.repository.ScheduleRepository;
import com.shiftplanner.shiftplanner_api.repository.ShiftAssignmentRepository;
import com.shiftplanner.shiftplanner_api.scheduling.SchedulingConstraints;
import com.shiftplanner.shiftplanner_api.scheduling.ShiftSlot;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class ScheduleGenerationService {

    private final ScheduleRepository scheduleRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final EmployeeRepository employeeRepository;

    private final WeeklyRequirementService weeklyRequirementService;
    private final AvailabilityService availabilityService;
    private final ScheduleIssueService scheduleIssueService;

    public ScheduleGenerationService(ScheduleRepository scheduleRepository, ShiftAssignmentRepository shiftAssignmentRepository,
                                     EmployeeRepository employeeRepository, WeeklyRequirementService weeklyRequirementService,
                                     AvailabilityService availabilityService, ScheduleIssueService scheduleIssueService) {

        this.scheduleRepository = scheduleRepository;
        this.shiftAssignmentRepository = shiftAssignmentRepository;
        this.employeeRepository = employeeRepository;
        this.weeklyRequirementService = weeklyRequirementService;
        this.availabilityService = availabilityService;
        this.scheduleIssueService = scheduleIssueService;
    }

    @Transactional
    public Schedule generateWeek(Store store, LocalDate weekStart) {
        SchedulingConstraints constraints = new SchedulingConstraints();
        LocalDate ws = weekStart.with(DayOfWeek.MONDAY);

        Schedule schedule = new Schedule();
        schedule.setStore(store);
        schedule.setWeek(ws);
        schedule.setStatus(Schedule.Status.DRAFT);
        schedule = scheduleRepository.save(schedule);

        //TODO: später: old cleanup wenn re-generate

        List<ShiftSlot> slots = weeklyRequirementService.loadShiftSlots(store, ws);
        Map<UUID, BigDecimal> assignedHours = new HashMap<>();
        Map<UUID, Set<LocalDate>> assignedDates = new HashMap<>();
        Map<UUID, Integer> earlyCount = new HashMap<>();
        Map<UUID, Integer> lateCount  = new HashMap<>();
        Map<UUID, Integer> satCount   = new HashMap<>();

        for (ShiftSlot slot : slots) {
            LocalDate date = slot.date();
            Availability.ShiftCode shift = slot.shiftCode();
            int required = slot.requiredHeadcount();

            List<Employee> all = employeeRepository.findByStoreAndActiveTrue(store);
            List<Employee> available = all.stream()
                    .filter(e -> availabilityService.isEmployeeAvailable(e, date, shift))
                    .toList();

            List<Employee> allowed = available.stream()
                    .filter(e -> constraints.canWork(e, date, shift))
                    .toList();

            List<Employee> candidates = allowed.stream()
                    .sorted(Comparator.comparing(e -> scoreCandidate(e, date, shift, assignedHours, earlyCount,lateCount,satCount)))
                    .toList();

//            List<Employee> candidates = employeeRepository.findByStoreAndActiveTrue(store);
//            candidates = candidates.stream()
//                    .filter(e -> constraints.canWork(e, date, shift))
//                    .filter(e -> availabilityService.isEmployeeAvailable(e, date, shift))
//                    .filter(e -> !assignedDates.getOrDefault(e.getId(), Set.of()).contains(date))
//                    .sorted(Comparator.comparing(e -> scoreCandidate(e, date, shift,assignedHours, earlyCount, lateCount, satCount)))
//                    .toList();

            int assignedCount = 0;
            int blockedByHours = 0;
            for (Employee e : candidates) {
                if (assignedCount >= required) break;

                BigDecimal shiftHours = hoursForShift(date, shift);

                BigDecimal current = assignedHours.getOrDefault(e.getId(), BigDecimal.ZERO);
                if (current.add(shiftHours).compareTo(e.getWeeklyHoursTarget()) > 0) {
                    blockedByHours++;
                    continue;
                }

                ShiftAssignment a = new ShiftAssignment();
                a.setSchedule(schedule);
                a.setDate(date);
                a.setEmployee(e);
                a.setShiftCode(shift);
                a.setAssignmentHours(shiftHours);
                shiftAssignmentRepository.save(a);

                assignedHours.put(e.getId(), current.add(shiftHours));
                assignedCount++;
                assignedDates.computeIfAbsent(e.getId(), id -> new HashSet<>()).add(date);

                if (shift == Availability.ShiftCode.EARLY) {
                    earlyCount.put(e.getId(), earlyCount.getOrDefault(e.getId(),0) + 1);
                }
                if (shift == Availability.ShiftCode.LATE) {
                    lateCount.put(e.getId(),lateCount.getOrDefault(e.getId(),0) + 1);
                }
                if (shift == Availability.ShiftCode.SAT_FULL) {
                    satCount.put(e.getId(), satCount.getOrDefault(e.getId(), 0) + 1);
                }
            }


            if (assignedCount < required) {
                if (all.isEmpty()) {
                    scheduleIssueService.reportIssue(schedule, ScheduleIssue.IssueType.NO_CANDIDATES,
                            "No active employee in store for " + date + " " + shift,
                            date, shift);
                } else if (available.isEmpty()) {
                    scheduleIssueService.reportIssue(schedule, ScheduleIssue.IssueType.NO_AVAILABLE_EMPLOYEES,
                            "No available employee for " + date + " " + shift,
                            date, shift);
                } else if (allowed.isEmpty()) {
                    scheduleIssueService.reportIssue(schedule, ScheduleIssue.IssueType.BLOCKED_DAY_CONFLICT,
                            "All available employee blocked by constraints fror " + date + " " +shift,
                            date, shift);
                } else if (blockedByHours >= candidates.size()) {
                    scheduleIssueService.reportIssue(schedule, ScheduleIssue.IssueType.HOURS_EXCEEDED,
                            "All candidates would exceed weekly hour limits for " + date + " " + shift,
                            date, shift);
                }else scheduleIssueService.reportIssue(
                        schedule,
                        ScheduleIssue.IssueType.UNDERSTAFFED,
                        "Not enough employees for " + date + " " + shift +
                                " (required=" + required + ", assigned=" + assignedCount + ")",
                        date,
                        shift
                );
            }
        }
        return schedule;
    }

    private List<Availability.ShiftCode> shiftForDate(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return List.of(Availability.ShiftCode.SAT_FULL);
        }
        return List.of(Availability.ShiftCode.EARLY, Availability.ShiftCode.LATE);
    }

    private BigDecimal hoursForShift(LocalDate date, Availability.ShiftCode shift) {
        if (shift == Availability.ShiftCode.SAT_FULL) return new BigDecimal(10);
        return new BigDecimal(7);
    }

    private double scoreCandidate(
            Employee e,
            LocalDate date,
            Availability.ShiftCode shift,
            Map<UUID, BigDecimal> assignedHours,
            Map<UUID, Integer> earlyCount,
            Map<UUID, Integer> lateCount,
            Map<UUID, Integer> satCount
    ) {
        UUID id = e.getId();

        double hours = assignedHours.getOrDefault(id, BigDecimal.ZERO).doubleValue();

        int early = earlyCount.getOrDefault(id, 0);
        int late = lateCount.getOrDefault(id, 0);
        int sat = satCount.getOrDefault(id, 0);

        //Grundidee: wenige Stunde ist gut

        double score = hours;

        // Extra-Penalty: wenn jemand schon oft denselben Shift hatte
        if (shift == Availability.ShiftCode.EARLY) score += early * 2.00;
        if (shift == Availability.ShiftCode.LATE) score += late * 2.00;
        if (shift == Availability.ShiftCode.SAT_FULL) score += sat * 3.00;

        return score;
    }
}
