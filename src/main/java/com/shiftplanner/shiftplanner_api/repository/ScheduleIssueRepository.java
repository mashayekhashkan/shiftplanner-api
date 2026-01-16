package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Schedule;
import com.shiftplanner.shiftplanner_api.domain.ScheduleIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleIssueRepository extends JpaRepository<ScheduleIssue, UUID> {

    List<ScheduleIssue> findBySchedule(Schedule schedule);
    List<ScheduleIssue> findByScheduleAndType(Schedule schedule, ScheduleIssue.IssueType type);
    boolean existsByScheduleAndTypeAndDateAndShiftCode(Schedule schedule, ScheduleIssue.IssueType type, LocalDate date, Availability.ShiftCode shiftCode);
    void deleteBySchedule(Schedule schedule);
}
