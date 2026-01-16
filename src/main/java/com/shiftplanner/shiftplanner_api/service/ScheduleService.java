package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Schedule;
import com.shiftplanner.shiftplanner_api.domain.Store;
import com.shiftplanner.shiftplanner_api.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    public List<Schedule> findByStoreAndWeek(Store store, LocalDate week) {
        return repository.findByStoreAndWeek(store, week);
    }

    public boolean hasSchedule (Store store, LocalDate week) {
        return repository.existsByStoreAndWeek(store, week);
    }
}
