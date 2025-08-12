package com.example.program.service;

import com.example.program.model.EnergyUsageRecord;
import com.example.program.repository.EnergyUsageRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class EnergyUsageService {

    private final EnergyUsageRecordRepository repo;

    public EnergyUsageService(EnergyUsageRecordRepository repo) {
        this.repo = repo;
    }

    public List<EnergyUsageRecord> findAll() { return repo.findAll(); }
    public Page<EnergyUsageRecord> findAll(Pageable pageable) { return repo.findAll(pageable); }

    public Optional<EnergyUsageRecord> get(Long id) { return repo.findById(id); }

    @Transactional
    public EnergyUsageRecord save(EnergyUsageRecord r) { return repo.save(r); }

    public List<EnergyUsageRecord> byAccount(String accountNo) { return repo.findByAccountNo(accountNo); }
    public List<EnergyUsageRecord> byDate(LocalDate date) { return repo.findByDate(date); }
    public List<EnergyUsageRecord> byRange(LocalDate start, LocalDate end) { return repo.findByDateBetween(start, end); }

    public BigDecimal dailyTotal(EnergyUsageRecord r) {
        if (r.getHourlyUsage() == null || r.getHourlyUsage().isEmpty()) return BigDecimal.ZERO;
        return r.getHourlyUsage().values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // hour with max kWh
    public LocalDateTime peakHour(EnergyUsageRecord r) {
        if (r.getHourlyUsage() == null || r.getHourlyUsage().isEmpty()) return null;
        return r.getHourlyUsage().entrySet().stream()
                .max(Map.Entry.comparingByValue())      // compares BigDecimal
                .map(Map.Entry::getKey)                  // returns LocalDateTime
                .orElse(null);
    }

    // totals by substation for a date
    public Map<String, BigDecimal> totalBySubstation(LocalDate date) {
        Map<String, BigDecimal> totals = new HashMap<>();
        for (EnergyUsageRecord r : byDate(date)) {      // your own fetch method
            totals.merge(r.getSubstation(), dailyTotal(r), BigDecimal::add);
        }
        return totals;
    }

    public Map<LocalDateTime, BigDecimal> systemLoadCurve(LocalDate date) {
        Map<LocalDateTime, BigDecimal> curve = new TreeMap<>(); // LocalDateTime is Comparable
        for (EnergyUsageRecord r : byDate(date)) {
            if (r.getHourlyUsage() == null) continue;
            r.getHourlyUsage().forEach((t, v) -> curve.merge(t, v, BigDecimal::add));
        }
        return curve;
    }
}
