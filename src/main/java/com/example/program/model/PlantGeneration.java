package com.example.program.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



@Entity
public class PlantGeneration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long timestamp;
    private String plantId;
    private String sourceKey;
    private double dcPower;
    private double acPower;
    private double dailyYield;
    private double totalYield;

    public PlantGeneration() {
    }

    public PlantGeneration(Long id, Long timestamp, String plantId, String sourceKey, double dcPower, double acPower, double dailyYield, double totalYield) {
        this.id = id;
        this.timestamp = timestamp;
        this.plantId = plantId;
        this.sourceKey = sourceKey;
        this.dcPower = dcPower;
        this.acPower = acPower;
        this.dailyYield = dailyYield;
        this.totalYield = totalYield;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public double getDcPower() {
        return dcPower;
    }

    public void setDcPower(double dcPower) {
        this.dcPower = dcPower;
    }

    public double getAcPower() {
        return acPower;
    }

    public void setAcPower(double acPower) {
        this.acPower = acPower;
    }

    public double getDailyYield() {
        return dailyYield;
    }

    public void setDailyYield(double dailyYield) {
        this.dailyYield = dailyYield;
    }

    public double getTotalYield() {
        return totalYield;
    }

    public void setTotalYield(double totalYield) {
        this.totalYield = totalYield;
    }
}
