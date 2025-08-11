package com.example.program.controllers;

import com.example.program.model.EnergyUsageRecord;
import com.example.program.service.EnergyUsageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/energy-usage")
public class EnergyUsageController {

    private final EnergyUsageService service;

    public EnergyUsageController(EnergyUsageService service) {
        this.service = service;
    }

    // GET all
    @GetMapping
    public List<EnergyUsageRecord> getAll() {
        return service.findAll();
    }

    // GET all (paged)
    @GetMapping("/page")
    public Page<EnergyUsageRecord> getAllPaged(
            @PageableDefault(size = 50) Pageable pageable) {
        return service.findAll(pageable);
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<EnergyUsageRecord> getById(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // POST create (simple passthrough)
    @PostMapping
    public ResponseEntity<EnergyUsageRecord> create(@RequestBody EnergyUsageRecord body) {
        return ResponseEntity.ok(service.save(body));
    }
}
