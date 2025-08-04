package com.example.program.controllers;

import com.example.program.model.PlantGeneration;
import com.example.program.repository.PlantGenerationRepository;
import com.example.program.service.PlantGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.*;
// @RestController marks this class as a controller where every method returns a domain object instead of a view.
// It's a shortcut for @Controller and @ResponseBody combined.
@RestController
// @RequestMapping defines the base URI for all endpoints in this class
// All methods here will be prefixed with /api/plants
@RequestMapping("/api/plants")
public class PlantController {


    private final PlantGenerationService service;

    public PlantController(PlantGenerationService service) {
        this.service = service;
    }




    /*############################################################
    Below are some implementations however are untested as of right now.
    Leaving this message here as a reminder to test at a later time.
     ############################################################*/




    // Return all records
    @GetMapping
    public List<PlantGeneration> getAllPlantData() {
        return service.getAll();
    }

    // Save a new record
    @PostMapping
    public PlantGeneration createPlantData(@RequestBody PlantGeneration newData) {
        return service.create(newData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantGeneration> updatePlantData(@PathVariable Long id, @RequestBody PlantGeneration updatedData) {
        return service.update(id, updatedData)
                .map(plant -> ResponseEntity.ok(plant))
                .orElse(ResponseEntity.notFound().build());
    }

}
