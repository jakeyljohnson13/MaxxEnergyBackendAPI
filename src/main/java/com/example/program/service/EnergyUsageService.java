package com.example.program.service;

import com.example.program.model.EnergyUsageRecord;
import com.example.program.repository.EnergyUsageRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnergyUsageService {

    private final EnergyUsageRecordRepository repo;

    @Transactional
    public EnergyUsageRecord save(EnergyUsageRecord r) {
        return repo.save(r);
    }

    public Optional<EnergyUsageRecord> get(Long id) {
        return repo.findById(id);
    }

    public List<EnergyUsageRecord> byAccount(String accountNo) {
        return repo.findByAccountNo(accountNo);
    }

    public List<EnergyUsageRecord> byDate(LocalDate date) {
        return repo.findByDate(date);
    }

    public List<EnergyUsageRecord> byRange(LocalDate start, LocalDate end) {
        return repo.findByDateBetween(start, end);
    }

    public double dailyTotal(EnergyUsageRecord r) {
        return r.getHourlyUsage() == null ? 0.0 :
                r.getHourlyUsage().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public String peakHour(EnergyUsageRecord r) {
        return r.getHourlyUsage() == null || r.getHourlyUsage().isEmpty() ? null :
                r.getHourlyUsage().entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey).orElse(null);
    }

    public Map<String, Double> totalBySubstation(LocalDate date) {
        return byDate(date).stream().collect(Collectors.groupingBy(
                EnergyUsageRecord::getSubstation,
                Collectors.summingDouble(this::dailyTotal)
        ));
    }

    public Map<String, Double> systemLoadCurve(LocalDate date) {
        Map<String, Double> curve = new TreeMap<>();
        for (EnergyUsageRecord r : byDate(date)) {
            if (r.getHourlyUsage() == null) continue;
            r.getHourlyUsage().forEach((t, v) -> curve.merge(t, v, Double::sum));
        }
        return curve;
    }
}