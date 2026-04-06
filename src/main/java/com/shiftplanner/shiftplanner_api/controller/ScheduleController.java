package com.shiftplanner.shiftplanner_api.controller;

import com.shiftplanner.shiftplanner_api.domain.Schedule;
import com.shiftplanner.shiftplanner_api.domain.Store;
import com.shiftplanner.shiftplanner_api.repository.StoreRepository;
import com.shiftplanner.shiftplanner_api.service.ScheduleGenerationService;
import com.shiftplanner.shiftplanner_api.service.ScheduleService;
import com.shiftplanner.shiftplanner_api.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleGenerationService scheduleGenerationService;
    private final StoreRepository storeRepository;

    public ScheduleController(ScheduleGenerationService  scheduleGenerationService, StoreRepository storeRepository) {
        this.scheduleGenerationService = scheduleGenerationService;
        this.storeRepository = storeRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerateScheduleResponse> generate(@RequestBody GenerateScheduleRequest request) {

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found: " + request.storeId()));

        LocalDate weekStart =  request.weekStart;

        Schedule schedule = scheduleGenerationService.generateWeek(store, weekStart);

        return ResponseEntity.ok(new GenerateScheduleResponse(
                schedule.getId(),
                schedule.getWeek(),
                schedule.getStatus().name()
        ));
    }


    public record GenerateScheduleRequest(UUID storeId, LocalDate weekStart) {}
    public record GenerateScheduleResponse(UUID scheduleId, LocalDate weekStart, String status) {}
}

