package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {

    List<Availability> findByEmployeeAndDayOfWeek (
            Employee employee,
            DayOfWeek dayOfWeek
    );

    boolean existsByEmployeeAndDayOfWeekAndShiftCodeAndAvailable(
            Employee employee,
            DayOfWeek dayOfWeek,
            Availability.ShiftCode shiftCode,
            boolean available
    );
}
