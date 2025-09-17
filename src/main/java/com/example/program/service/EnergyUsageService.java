package com.example.program.service;


import com.example.program.data.UsagePoint;
import com.example.program.repository.EnergyUsageRecordRepository;
import com.example.program.repository.EnergyUsageRecordRepository.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class EnergyUsageService {

    private final EnergyUsageRecordRepository repo;

    public EnergyUsageService(EnergyUsageRecordRepository repo) {
        this.repo = repo;
    }

    private static LocalDateTime startOf(LocalDate d) {
        return d.atStartOfDay();
    }

    private static LocalDateTime startOfNext(LocalDate d) {
        return d.plusDays(1).atStartOfDay();
    }

    public List<UsagePoint> usagePoints(String accountNo, String substation,
                                        LocalDate from, LocalDate to) {

        ZoneId zone = ZoneOffset.UTC; // or your DB/server zone
        return repo.usagePoints(accountNo, substation,
                        from.atStartOfDay(), to.plusDays(1).atStartOfDay())
                .stream()
                .map((UsagePointView v) -> {
                    Instant inst = v.getTs().atZone(zone).toInstant();
                    return new UsagePoint(
                            inst, v.getKwh(), v.getAccountNo(),
                            v.getSubstation(), v.getType(), v.getZipCode());
                })
                .toList();
    }

    public List<DailyUsageRow> dailyUsage(String accountNo, String substation, LocalDate from, LocalDate to) {
        return repo.dailyUsage(accountNo, substation, startOf(from), startOfNext(to));
    }

    public List<CumulativeUsageRow> cumulativeUsage(String accountNo, String substation, LocalDate from, LocalDate to) {
        return repo.cumulativeUsage(accountNo, substation, startOf(from), startOfNext(to));
    }

    public List<UsageSummaryRow> usageSummaries(String accountNo, String substation, LocalDate from, LocalDate to) {
        return repo.usageSummaries(accountNo, substation, startOf(from), startOfNext(to));
    }

    public List<SubstationLoadRow> substationLoad(String substation, LocalDate from, LocalDate to) {
        return repo.substationLoad(substation, startOf(from), startOfNext(to));
    }
}
