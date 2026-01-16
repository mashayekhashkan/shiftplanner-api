package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Availability;
import com.shiftplanner.shiftplanner_api.domain.Employee;
import com.shiftplanner.shiftplanner_api.repository.AvailabilityRepository;
import com.shiftplanner.shiftplanner_api.repository.EmployeeBlockDayRepository;
import com.shiftplanner.shiftplanner_api.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository repository;
    private final EmployeeBlockDayRepository blockDayRepository;

    public AvailabilityService(AvailabilityRepository repository, EmployeeBlockDayRepository blockDayRepository) {
        this.repository = repository;
        this.blockDayRepository = blockDayRepository;
    }

    public List<Availability> getByEmployeeAndDayOfWeek(Employee employee, DayOfWeek dayOfWeek) {
        return repository.findByEmployeeAndDayOfWeek(employee, dayOfWeek);
    }

    public boolean isAktive (Employee employee, DayOfWeek dayOfWeek, Availability.ShiftCode shiftCode) {
        return repository.existsByEmployeeAndDayOfWeekAndAvailableTreu(employee, dayOfWeek, shiftCode);
    }

    public boolean isEmployeeAvailable (Employee employee, LocalDate date, Availability.ShiftCode shiftCode) {

        if(blockDayRepository.existsBayEmployeeAndDate(employee,date)) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return repository.existsByEmployeeAndDayOfWeekAndAvailableTreu(employee, dayOfWeek, shiftCode);
    }
}
