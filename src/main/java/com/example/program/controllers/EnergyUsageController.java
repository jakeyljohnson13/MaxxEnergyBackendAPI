package com.example.program.controllers;

import com.example.program.model.EnergyUsageRecord;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnergyUsageController {

    @GetMapping("/mock")
    public List<EnergyUsageRecord> getMockUsage() {
        Map<String, Double> hourly = new LinkedHashMap<>();
        String[] times = {"0:00", "0:30", "1:00", "1:30", "2:00", "2:30"};
        double[] values = {1.224, 1.284, 1.286, 0.846, 0.826, 1.341};

        for (int i = 0; i < times.length; i++) {
            hourly.put(times[i], values[i]);
        }

        EnergyUsageRecord record = new EnergyUsageRecord(
                "1300000001", "R", "SUBST402", "W", "TRNSF135",
                "23227", LocalDate.of(2024, 1, 1), hourly
        );

        return List.of(record);
    }
}
