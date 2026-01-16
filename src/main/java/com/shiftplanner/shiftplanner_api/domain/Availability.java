package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Table(name = "availability")
@Getter
@Setter
@NoArgsConstructor
public class Availability {

    public enum ShiftCode{
        EARLY,LATE,SAT_FULL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftCode shiftCode;
    @Column(nullable = false)
    private boolean available;
}
