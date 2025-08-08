package com.example.program.repository;

import com.example.program.model.EnergyUsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EnergyUsageRecordRepository extends JpaRepository<EnergyUsageRecord, Long> {
    List<EnergyUsageRecord> findByAccountNo(String accountNo);
    List<EnergyUsageRecord> findByDate(LocalDate date);
    List<EnergyUsageRecord> findByDateBetween(LocalDate start, LocalDate end);
    List<EnergyUsageRecord> findBySubstation(String substation);
}