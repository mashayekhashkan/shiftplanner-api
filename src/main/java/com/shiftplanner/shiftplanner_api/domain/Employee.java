package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal weeklyHoursTarget;
    @Column(nullable = false)
    private boolean saturdayOnly;
    @Column(nullable = false)
    private boolean onlyEarlyShift;
    @Column(nullable = false)
    private boolean onlyLateShift;
}
