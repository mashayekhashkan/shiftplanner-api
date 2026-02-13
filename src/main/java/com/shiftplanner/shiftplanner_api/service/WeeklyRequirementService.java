package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Store;
import com.shiftplanner.shiftplanner_api.domain.WeeklyRequirement;
import com.shiftplanner.shiftplanner_api.repository.WeeklyRequirementRepository;
import com.shiftplanner.shiftplanner_api.scheduling.ShiftSlot;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class WeeklyRequirementService {

    private final WeeklyRequirementRepository repository;

    public WeeklyRequirementService(WeeklyRequirementRepository repository) {
        this.repository = repository;
    }

    public List<WeeklyRequirement> getForWork (Store store, LocalDate weekStart) {
        return repository.findByStoreAndWeekStart(store, weekStart);
    }

    public int getRequiredHeadcount(
            Store store,
            LocalDate date,
            Availability.ShiftCode shift
    ) {
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return repository
                .findByStoreAndWeekStartAndDayOfWeekAndShiftCode(
                        store,
                        weekStart,
                        dayOfWeek,
                        shift
                )
                .map(WeeklyRequirement::getRequiredHeadcount)
                .orElse(0);
    }

    public boolean hasRequirement (Store store, LocalDate date, DayOfWeek dayOfWeek, Availability.ShiftCode shiftCode) {
        return repository.existsByStoreAndWeekStartAndDayOfWeekAndShiftCode(store, date, dayOfWeek, shiftCode);
    }

    public List<ShiftSlot> loadShiftSlots(Store store, LocalDate weekStart) {
        LocalDate ws = weekStart.with(DayOfWeek.MONDAY);

        List<WeeklyRequirement> reqs =
                repository.findByStoreAndWeekStart(store, ws);

        return reqs.stream()
                .map(req -> new ShiftSlot(
                        ws.plusDays(ws.getDayOfWeek().getValue() -1),
                        req.getDayOfWeek(),
                        req.getShiftCode(),
                        req.getRequiredHeadcount()
                ))
                .toList();
    }

}
