package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Employee;
import com.shiftplanner.shiftplanner_api.repository.EmployeeBlockDayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmployeeBlockDayService {

    private final EmployeeBlockDayRepository repository;

    public EmployeeBlockDayService(EmployeeBlockDayRepository repository) {
        this.repository = repository;
    }

    public boolean isEmployeeBlocked (Employee employee, LocalDate date) {
        return repository.existsByEmployeeAndDate(employee, date);
    }
}
