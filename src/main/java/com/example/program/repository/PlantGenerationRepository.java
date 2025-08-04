package com.example.program.repository;

import com.example.program.model.PlantGeneration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantGenerationRepository extends JpaRepository<PlantGeneration, Long> {
}
