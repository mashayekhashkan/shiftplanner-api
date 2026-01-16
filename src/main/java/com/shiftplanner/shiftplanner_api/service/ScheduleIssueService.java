package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Schedule;
import com.shiftplanner.shiftplanner_api.domain.ScheduleIssue;
import com.shiftplanner.shiftplanner_api.repository.ScheduleIssueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleIssueService {

    private final ScheduleIssueRepository repository;

    public ScheduleIssueService(ScheduleIssueRepository repository) {
        this.repository = repository;
    }

    public List<ScheduleIssue> getBySchedule(Schedule schedule) {
        return repository.findBySchedule(schedule);
    }
    public List<ScheduleIssue> getByScheduleAndType(Schedule schedule, ScheduleIssue.IssueType type) {
        return repository.findByScheduleAndType(schedule, type);
    }

    public boolean hasScheduleIssue(Schedule schedule, ScheduleIssue.IssueType type, LocalDate date, Availability.ShiftCode shiftCode) {
        return repository.existsByScheduleAndTypeAndDateAndShiftCode(schedule, type, date, shiftCode);
    }

    public void reportIssue(
            Schedule schedule,
            ScheduleIssue.IssueType type,
            String message,
            LocalDate date,
            Availability.ShiftCode shiftCode
    ) {
        if (repository.existsByScheduleAndTypeAndDateAndShiftCode(schedule, type, date, shiftCode)) {
            return;
        }

        ScheduleIssue issue = new ScheduleIssue();
        issue.setSchedule(schedule);
        issue.setType(type);
        issue.setMessage(message);
        issue.setDate(date);
        issue.setShiftCode(shiftCode);

        repository.save(issue);
    }

    public void clearIssues(Schedule schedule) {
        repository.deleteBySchedule(schedule);
    }
}
