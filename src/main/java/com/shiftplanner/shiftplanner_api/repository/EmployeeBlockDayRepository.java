package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Employee;
import com.shiftplanner.shiftplanner_api.domain.EmployeeBlockDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface EmployeeBlockDayRepository extends JpaRepository<EmployeeBlockDay, UUID> {

    boolean existsBayEmployeeAndDate(Employee employee, LocalDate date);
}
