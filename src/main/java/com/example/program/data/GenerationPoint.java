package com.example.program.data;

public record GenerationPoint(
        String time,
        Double acPower,
        Double dcPower,
        Double dailyYield,
        Double totalYield,
        String plantId
) {
    public GenerationPoint withTime(String newTime) {
        return new GenerationPoint(newTime, acPower, dcPower, dailyYield, totalYield, plantId);
    }
}

