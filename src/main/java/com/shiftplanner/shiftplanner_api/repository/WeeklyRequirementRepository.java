package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Store;
import com.shiftplanner.shiftplanner_api.domain.WeeklyRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyRequirementRepository extends JpaRepository<WeeklyRequirement, UUID> {

    List<WeeklyRequirement> findStoreAndWeekStart(Store store, LocalDate weekStart);
    Optional<WeeklyRequirement> findByStoreAndWeekStartAndDayOfWeekAndShiftCode(
            Store store,
            LocalDate weekStart,   // ← DAS ist kein DayOfWeek
            DayOfWeek dayOfWeek,
            Availability.ShiftCode shiftCode
    );
    boolean existsByStoreAndWeekStartAndDayOfWeekAndShiftCode(Store store, LocalDate date, DayOfWeek dayOfWeek, Availability.ShiftCode shiftCode);
}
