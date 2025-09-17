package com.example.program.repository;

import com.example.program.model.EnergyUsageRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnergyUsageRecordRepository extends JpaRepository<EnergyUsageRecord, Long> {

    // ========== EXISTING BASIC QUERIES ==========
    List<EnergyUsageRecord> findByAccountNo(String accountNo);
    List<EnergyUsageRecord> findByDate(LocalDate date);
    List<EnergyUsageRecord> findByDateBetween(LocalDate start, LocalDate end);
    List<EnergyUsageRecord> findBySubstation(String substation);

    // ========== ADVANCED QUERIES ==========

    // Find records ordered by date for pagination
    List<EnergyUsageRecord> findByOrderByDateDesc(Pageable pageable);

    // Find records by date range with ordering
    List<EnergyUsageRecord> findByDateBetweenOrderByDateAsc(LocalDate start, LocalDate end);

    // Find records by account and date range
    List<EnergyUsageRecord> findByAccountNoAndDateBetweenOrderByDateAsc(String accountNo, LocalDate start, LocalDate end);

    // Find latest and earliest records
    Optional<EnergyUsageRecord> findTopByOrderByDateDesc();
    Optional<EnergyUsageRecord> findTopByOrderByDateAsc();

    // ========== PROJECTION INTERFACES FOR ANALYTICS ==========


    // -------- Projection types --------
    interface UsagePointView {
        LocalDateTime getTs();
        BigDecimal getKwh();
        String getAccountNo();
        String getSubstation();
        String getType();
        String getZipCode();
    }

    interface DailyUsageRow {
        LocalDate getDay();
        String getAccountNo();
        String getSubstation();
        BigDecimal getTotalKwh();
    }

    interface CumulativeUsageRow {
        LocalDate getDay();
        String getAccountNo();
        String getSubstation();
        BigDecimal getCumulativeKwh();
    }

    interface UsageSummaryRow {
        String getAccountNo();
        String getSubstation();
        LocalDate getStartDate();
        LocalDate getEndDate();
        Long getRecordCount();
        BigDecimal getTotalKwh();
    }

    interface SubstationLoadRow {
        String getSubstation();
        LocalDate getDay();
        BigDecimal getTotalLoad();
        Long getAccountCount();
        BigDecimal getAvgLoadPerAccount();
    }

    // -------- Queries --------

    // Raw hourly points for charts
    @Query(value = """
        SELECT 
            euh.timestamp               AS ts,
            euh.kwh                     AS kwh,
            eur.account_no              AS accountNo,
            eur.substation              AS substation,
            eur.type                    AS type,
            eur.zip_code                AS zipCode
        FROM energy_usage_hours euh
        JOIN energy_usage_record eur ON eur.id = euh.record_id
        WHERE (:accountNo IS NULL OR eur.account_no = :accountNo)
          AND (:substation IS NULL OR eur.substation = :substation)
          AND euh.timestamp >= :fromTs
          AND euh.timestamp <  :toTs
        ORDER BY euh.timestamp ASC
        """, nativeQuery = true)
    List<UsagePointView> usagePoints(
            @Param("accountNo") String accountNo,
            @Param("substation") String substation,
            @Param("fromTs") LocalDateTime fromTs,
            @Param("toTs") LocalDateTime toTs);

    // Daily totals by account/substation
    @Query(value = """
        SELECT
            DATE(euh.timestamp)         AS day,
            eur.account_no              AS accountNo,
            eur.substation              AS substation,
            SUM(euh.kwh)                AS totalKwh
        FROM energy_usage_hours euh
        JOIN energy_usage_record eur ON eur.id = euh.record_id
        WHERE (:accountNo IS NULL OR eur.account_no = :accountNo)
          AND (:substation IS NULL OR eur.substation = :substation)
          AND euh.timestamp >= :fromTs
          AND euh.timestamp <  :toTs
        GROUP BY day, accountNo, substation
        ORDER BY day ASC
        """, nativeQuery = true)
    List<DailyUsageRow> dailyUsage(
            @Param("accountNo") String accountNo,
            @Param("substation") String substation,
            @Param("fromTs") LocalDateTime fromTs,
            @Param("toTs") LocalDateTime toTs);

    // Daily cumulative kWh by account/substation (window fn requires MySQL 8+)
    @Query(value = """
        SELECT
            t.day                       AS day,
            t.account_no                AS accountNo,
            t.substation                AS substation,
            SUM(t.total_kwh) OVER (
                PARTITION BY t.account_no, t.substation
                ORDER BY t.day
                ROWS UNBOUNDED PRECEDING
            )                           AS cumulativeKwh
        FROM (
            SELECT
                DATE(euh.timestamp)     AS day,
                eur.account_no          AS account_no,
                eur.substation          AS substation,
                SUM(euh.kwh)            AS total_kwh
            FROM energy_usage_hours euh
            JOIN energy_usage_record eur ON eur.id = euh.record_id
            WHERE (:accountNo IS NULL OR eur.account_no = :accountNo)
              AND (:substation IS NULL OR eur.substation = :substation)
              AND euh.timestamp >= :fromTs
              AND euh.timestamp <  :toTs
            GROUP BY day, account_no, substation
        ) t
        ORDER BY t.day ASC
        """, nativeQuery = true)
    List<CumulativeUsageRow> cumulativeUsage(
            @Param("accountNo") String accountNo,
            @Param("substation") String substation,
            @Param("fromTs") LocalDateTime fromTs,
            @Param("toTs") LocalDateTime toTs);

    // Overall summary across the range
    @Query(value = """
        SELECT
            eur.account_no                          AS accountNo,
            eur.substation                          AS substation,
            MIN(DATE(euh.timestamp))                AS startDate,
            MAX(DATE(euh.timestamp))                AS endDate,
            COUNT(*)                                AS recordCount,
            SUM(euh.kwh)                            AS totalKwh
        FROM energy_usage_hours euh
        JOIN energy_usage_record eur ON eur.id = euh.record_id
        WHERE (:accountNo IS NULL OR eur.account_no = :accountNo)
          AND (:substation IS NULL OR eur.substation = :substation)
          AND euh.timestamp >= :fromTs
          AND euh.timestamp <  :toTs
        GROUP BY eur.account_no, eur.substation
        ORDER BY eur.account_no, eur.substation
        """, nativeQuery = true)
    List<UsageSummaryRow> usageSummaries(
            @Param("accountNo") String accountNo,
            @Param("substation") String substation,
            @Param("fromTs") LocalDateTime fromTs,
            @Param("toTs") LocalDateTime toTs);

    // Substation load view (sum + per-account average)
    @Query(value = """
        SELECT
            eur.substation                          AS substation,
            DATE(euh.timestamp)                     AS day,
            SUM(euh.kwh)                            AS totalLoad,
            COUNT(DISTINCT eur.account_no)          AS accountCount,
            (SUM(euh.kwh) / NULLIF(COUNT(DISTINCT eur.account_no),0))
                                                    AS avgLoadPerAccount
        FROM energy_usage_hours euh
        JOIN energy_usage_record eur ON eur.id = euh.record_id
        WHERE (:substation IS NULL OR eur.substation = :substation)
          AND euh.timestamp >= :fromTs
          AND euh.timestamp <  :toTs
        GROUP BY substation, day
        ORDER BY substation, day
        """, nativeQuery = true)
    List<SubstationLoadRow> substationLoad(
            @Param("substation") String substation,
            @Param("fromTs") LocalDateTime fromTs,
            @Param("toTs") LocalDateTime toTs);
}