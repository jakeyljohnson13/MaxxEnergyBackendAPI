package com.example.program.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnergyUsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNo;
    private String type;
    private String substation;
    private String season;
    private String transformer;
    private String zipCode;
    private LocalDate date;
    @ElementCollection
    @CollectionTable(name = "energy_usage_hours", joinColumns = @JoinColumn(name = "record_id"))
    @MapKeyColumn(name = "time_hhmm")
    @Column(name = "usage_kw")
    private Map<String, Double> hourlyUsage = new LinkedHashMap<>();

}
