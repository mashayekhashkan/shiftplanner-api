package com.shiftplanner.shiftplanner_api.scheduling;

import com.shiftplanner.shiftplanner_api.domain.Availability;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record ShiftSlot(
        LocalDate date,
        DayOfWeek dayOfWeek,
        Availability.ShiftCode shiftCode,
        int requiredHeadcount
) {}
