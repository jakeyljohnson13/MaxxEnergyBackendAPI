package com.example.program.controllers;

import com.example.program.model.PlantGeneration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
// @RestController tells Spring this class handles web requests and returns JSON data
@RestController
// @RequestMapping defines the base URL for all methods in this controller
@RequestMapping("/api/plants")
public class PlantController {

    // @GetMapping makes this method respond to GET requests at /api/plants/demo
    @GetMapping("/demo")
    public List<PlantGeneration> getDemoData() {
        // Create a couple of hardcoded PlantGeneration objects
        PlantGeneration demo1 = new PlantGeneration(
                4135001L,
                "1BY6WEcLGh8j5v7",
                "SOURCE001",
                200.5,
                195.0,
                875.2,
                1000000.0
        );

        PlantGeneration demo2 = new PlantGeneration(
                4135002L,
                "3PZuoBAID5Wc2HD",
                "SOURCE002",
                180.0,
                178.5,
                600.0,
                950000.0
        );

        // Return both in a list — Spring Boot will automatically convert this to JSON
        return List.of(demo1, demo2);


    }
}
