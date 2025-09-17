package com.example.program.controllers;

import com.example.program.data.UsagePoint;
import com.example.program.repository.EnergyUsageRecordRepository.*;
import com.example.program.service.EnergyUsageService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public class EnergyUsageController {

    private final EnergyUsageService svc;

    public EnergyUsageController(EnergyUsageService svc) {
        this.svc = svc;
    }

    @GetMapping("/points")
    public List<UsagePoint> points(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String substation
    ) {
        return svc.usagePoints(accountNo, substation, from, to);
    }

    @GetMapping("/daily")
    public List<DailyUsageRow> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String substation
    ) {
        return svc.dailyUsage(accountNo, substation, from, to);
    }

    @GetMapping("/cumulative")
    public List<CumulativeUsageRow> cumulative(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String substation
    ) {
        return svc.cumulativeUsage(accountNo, substation, from, to);
    }

    @GetMapping("/summary")
    public List<UsageSummaryRow> summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String substation
    ) {
        return svc.usageSummaries(accountNo, substation, from, to);
    }

    @GetMapping("/substation-load")
    public List<SubstationLoadRow> substationLoad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String substation
    ) {
        return svc.substationLoad(substation, from, to);
    }

}
