package com.example.program.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table
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

    // ----- JPA requires a no-args constructor -----
    protected EnergyUsageRecord() {}

    // ----- Optional all-args constructor -----
    public EnergyUsageRecord(Long id, String accountNo, String type, String substation,
                             String season, String transformer, String zipCode,
                             LocalDate date, Map<String, Double> hourlyUsage) {
        this.id = id;
        this.accountNo = accountNo;
        this.type = type;
        this.substation = substation;
        this.season = season;
        this.transformer = transformer;
        this.zipCode = zipCode;
        this.date = date;
        this.hourlyUsage = (hourlyUsage != null) ? hourlyUsage : new LinkedHashMap<>();
    }

    // ----- Manual builder (no Lombok needed) -----
    public static Builder builder() { return new Builder(); }
    public static final class Builder {
        private Long id;
        private String accountNo, type, substation, season, transformer, zipCode;
        private LocalDate date;
        private Map<String, Double> hourlyUsage = new LinkedHashMap<>();

        public Builder id(Long v){ this.id=v; return this; }
        public Builder accountNo(String v){ this.accountNo=v; return this; }
        public Builder type(String v){ this.type=v; return this; }
        public Builder substation(String v){ this.substation=v; return this; }
        public Builder season(String v){ this.season=v; return this; }
        public Builder transformer(String v){ this.transformer=v; return this; }
        public Builder zipCode(String v){ this.zipCode=v; return this; }
        public Builder date(LocalDate v){ this.date=v; return this; }
        public Builder hourlyUsage(Map<String, Double> v){ this.hourlyUsage=v; return this; }

        public EnergyUsageRecord build() {
            return new EnergyUsageRecord(id, accountNo, type, substation, season,
                    transformer, zipCode, date,
                    hourlyUsage != null ? hourlyUsage : new LinkedHashMap<>());
        }
    }

    // ----- Getters & setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubstation() { return substation; }
    public void setSubstation(String substation) { this.substation = substation; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public String getTransformer() { return transformer; }
    public void setTransformer(String transformer) { this.transformer = transformer; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Map<String, Double> getHourlyUsage() { return hourlyUsage; }
    public void setHourlyUsage(Map<String, Double> hourlyUsage) { this.hourlyUsage = hourlyUsage; }
}
