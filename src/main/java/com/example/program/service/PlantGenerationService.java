package com.example.program.service;

import com.example.program.model.PlantGeneration;
import com.example.program.repository.PlantGenerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantGenerationService {

    private final PlantGenerationRepository repository;

    public PlantGenerationService(PlantGenerationRepository repository) {
        this.repository = repository;
    }

    public List<PlantGeneration> getAll() {
        return repository.findAll();
    }

    public PlantGeneration create(PlantGeneration data) {
        return repository.save(data);
    }

    public Optional<PlantGeneration> update(Long id, PlantGeneration updatedData) {
        return repository.findById(id).map(existing -> {
            existing.setTimestamp(updatedData.getTimestamp());
            existing.setPlantId(updatedData.getPlantId());
            existing.setSourceKey(updatedData.getSourceKey());
            existing.setDcPower(updatedData.getDcPower());
            existing.setAcPower(updatedData.getAcPower());
            existing.setDailyYield(updatedData.getDailyYield());
            existing.setTotalYield(updatedData.getTotalYield());
            return repository.save(existing);
        });
    }
}
