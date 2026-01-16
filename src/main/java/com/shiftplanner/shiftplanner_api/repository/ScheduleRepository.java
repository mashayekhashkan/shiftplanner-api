package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Schedule;
import com.shiftplanner.shiftplanner_api.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    List<Schedule> findByStoreAndWeek(Store store, LocalDate week);
    boolean existsByStoreAndWeek(Store store, LocalDate week);
}
