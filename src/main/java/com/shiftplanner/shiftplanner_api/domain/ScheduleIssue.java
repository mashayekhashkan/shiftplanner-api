package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "scheduleIssue")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleIssue {

    public enum IssueType {
        UNDERSTAFFED,
        HOURS_EXCEEDED,
        NO_AVAILABLE_EMPLOYEES,
        SATURDAY_QUOTA_NOT_MET,
        BLOCKED_DAY_CONFLICT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private IssueType type;
    @Column(nullable = false, length = 500)
    private String message;
    @Column(name = "issue_date")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @Column(name = "shift_code", length = 10)
    private Availability.ShiftCode shiftCode;
}
