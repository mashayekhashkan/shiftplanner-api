package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Schedule;
import com.shiftplanner.shiftplanner_api.domain.ShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, UUID > {

    List<ShiftAssignment> findBySchedule(Schedule schedule);
    void deleteBySchedule (Schedule schedule);
}
