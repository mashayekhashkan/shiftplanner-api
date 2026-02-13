package com.shiftplanner.shiftplanner_api.scheduling;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Employee;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class SchedulingConstraints {

    public boolean canWork(Employee e, LocalDate date, Availability.ShiftCode shift) {

        if (!e.isActive()) return false;

        //SaturdayOnly
        if (e.isSaturdayOnly() && date.getDayOfWeek() != DayOfWeek.SATURDAY) return false;

        //onlyEarlyShift / onlyLateShift
        if (e.isOnlyEarlyShift() && shift != Availability.ShiftCode.EARLY) return false;
        if (e.isOnlyLateShift() && shift != Availability.ShiftCode.LATE) return false;

        //Saturday full shift only on saturday
        if (shift == Availability.ShiftCode.SAT_FULL && date.getDayOfWeek() != DayOfWeek.SATURDAY) return false;

        return true;
    }
}
