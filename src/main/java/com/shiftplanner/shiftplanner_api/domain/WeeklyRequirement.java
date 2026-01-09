package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "weekly_requirement")
@Getter
@Setter
@NoArgsConstructor
public class WeeklyRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(nullable = false)
    private LocalDate weekStart;
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;
    @JoinColumn(name = "shift_code")
    private Availability.ShiftCode shiftCode;
    @Min((0))
    @Max(10)
    @Column(name = "required_headcount", nullable = false)
    private int requiredHeadcount;
}
