package com.example.program.model;

import java.time.LocalDate;
import java.util.Map;

public class EnergyUsageRecord {
    private String accountNo;
    private String type;
    private String substation;
    private String season;
    private String transformer;
    private String zipCode;
    private LocalDate date;
    private Map<String, Double> hourlyUsage; // key = "HH:mm", value = usage

    public EnergyUsageRecord() {}

    public EnergyUsageRecord(String accountNo, String type, String substation, String season,
                             String transformer, String zipCode, LocalDate date, Map<String, Double> hourlyUsage) {
        this.accountNo = accountNo;
        this.type = type;
        this.substation = substation;
        this.season = season;
        this.transformer = transformer;
        this.zipCode = zipCode;
        this.date = date;
        this.hourlyUsage = hourlyUsage;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getType() {
        return type;
    }

    public String getSubstation() {
        return substation;
    }

    public String getSeason() {
        return season;
    }

    public String getTransformer() {
        return transformer;
    }

    public String getZipCode() {
        return zipCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<String, Double> getHourlyUsage() {
        return hourlyUsage;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubstation(String substation) {
        this.substation = substation;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHourlyUsage(Map<String, Double> hourlyUsage) {
        this.hourlyUsage = hourlyUsage;
    }
}
