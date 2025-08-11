package com.example.program.service;

import com.example.program.model.EnergyUsageRecord;
import com.example.program.repository.EnergyUsageRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public double dailyTotal(EnergyUsageRecord r) {
        return r.getHourlyUsage() == null ? 0.0
                : r.getHourlyUsage().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public String peakHour(EnergyUsageRecord r) {
        if (r.getHourlyUsage() == null || r.getHourlyUsage().isEmpty()) return null;
        return r.getHourlyUsage().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }

    public Map<String, Double> totalBySubstation(LocalDate date) {
        Map<String, Double> totals = new HashMap<>();
        for (EnergyUsageRecord r : byDate(date)) {
            String key = r.getSubstation();
            totals.merge(key, dailyTotal(r), Double::sum);
        }
        return totals;
    }

    public Map<String, Double> systemLoadCurve(LocalDate date) {
        Map<String, Double> curve = new TreeMap<>(); // sorted by time key
        for (EnergyUsageRecord r : byDate(date)) {
            if (r.getHourlyUsage() == null) continue;
            r.getHourlyUsage().forEach((t, v) -> curve.merge(t, v, Double::sum));
        }
        return curve;
    }
}
