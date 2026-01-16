package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Employee;
import com.shiftplanner.shiftplanner_api.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployeesByName (String name) {
        return repository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase(name, name);
    }

    public Employee getEmployeeById (UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                     new IllegalArgumentException("Employee not found: " + id));
    }
}
