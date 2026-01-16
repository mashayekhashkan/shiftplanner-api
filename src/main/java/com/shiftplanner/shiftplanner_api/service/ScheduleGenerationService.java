package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.*;
import com.shiftplanner.shiftplanner_api.repository.EmployeeBlockDayRepository;
import com.shiftplanner.shiftplanner_api.repository.EmployeeRepository;
import com.shiftplanner.shiftplanner_api.repository.ScheduleRepository;
import com.shiftplanner.shiftplanner_api.repository.ShiftAssignmentRepository;
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
        LocalDate ws = weekStart.with(DayOfWeek.MONDAY);

        Schedule schedule = new Schedule();
        schedule.setStore(store);
        schedule.setWeek(ws);
        schedule.setStatus(Schedule.Status.DRAFT);
        schedule = scheduleRepository.save(schedule);

        //TODO: später: old cleanup wenn re-generate

        Map<UUID, BigDecimal> assignedHours = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = ws.plusDays(i);

            List<Availability.ShiftCode> shifts = shiftForDate(date);

            for (Availability.ShiftCode shift : shifts) {
                int required = weeklyRequirementService.getRequiredHeadcount(store, date, shift);

                List<Employee> candidates = employeeRepository.findByStoreAndAktiveTreu(store);
                candidates = candidates.stream()
                        .filter(e -> availabilityService.isEmployeeAvailable(e, date, shift))
                        .sorted(Comparator.comparing(e -> assignedHours.getOrDefault(e.getId(), BigDecimal.ZERO)))
                        .toList();

                int assignedCount = 0;
                for (Employee e : candidates) {
                    if (assignedCount >= required) break;

                    BigDecimal shiftHours = hoursForShift(date, shift);

                    BigDecimal currant = assignedHours.getOrDefault(e.getId(), BigDecimal.ZERO);
                    if (currant.add(shiftHours).compareTo(e.getWeeklyHoursTarget()) > 0) {
                        continue;
                    }

                    ShiftAssignment a  = new ShiftAssignment();
                    a.setSchedule(schedule);
                    a.setDate(date);
                    a.setEmployee(e);
                    a.setShiftCode(shift);
                    a.setAssignmentHours(shiftHours);
                    shiftAssignmentRepository.save(a);

                    assignedHours.put(e.getId(), currant.add(shiftHours));
                    assignedCount++;
                }

                if (assignedCount > required) {
                    scheduleIssueService.reportIssue(
                            schedule,
                            ScheduleIssue.IssueType.UNDERSTAFFED,
                            "Not enough employees for " + date + " " + shift + " (required=" + required + ", assigned=" + assignedCount + ")",
                            date,
                            shift
                    );
                }
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
}
