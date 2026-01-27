package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Employee;
import com.shiftplanner.shiftplanner_api.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(String firstName, String lastName);
    List<Employee> findByStoreAndActiveTrue(Store store);
}
