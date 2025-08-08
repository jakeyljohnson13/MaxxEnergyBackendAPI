package com.example.program.controllers;

import com.example.program.model.EnergyUsageRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// REST controller for handling Energy Usage API endpoints
// Base URL: /api/energy-usage
@RestController
@RequestMapping("/api/energy-usage")

public class EnergyUsageController {

    @GetMapping("/mock")
    public List<EnergyUsageRecord> getMockUsage() {
        Map<String, Double> hourly = new LinkedHashMap<>();
        String[] times = {"0:00", "0:30", "1:00", "1:30", "2:00", "2:30"};
        double[] values = {1.224, 1.284, 1.286, 0.846, 0.826, 1.341};

        for (int i = 0; i < times.length; i++) {
            hourly.put(times[i], values[i]);
        }


        EnergyUsageRecord record = EnergyUsageRecord.builder()
                .accountNo("1300000001")
                .type("R")
                .substation("SUBST402")
                .season("W")
                .transformer("TRNSF135")
                .zipCode("23227")
                .date(LocalDate.of(2024, 1, 1))
                .hourlyUsage(hourly)
                .build();
        return List.of(record);
    }
}
