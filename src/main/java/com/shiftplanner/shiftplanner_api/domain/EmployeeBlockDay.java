package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "employee_block_day")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeBlockDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Employee employee_id;
    @Column(nullable = false)
    private LocalDate date;
}
