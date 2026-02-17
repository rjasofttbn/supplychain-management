package com.example.supplyChainSystem.farmer.controller;

import com.example.supplyChainSystem.farmer.entity.Farmer;
import com.example.supplyChainSystem.farmer.service.FarmerService;
import com.example.supplyChainSystem.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {
    private final FarmerService farmerService;

    @GetMapping
    public ResponseEntity<List<Farmer>> getAll() {
        return ResponseEntity.ok(farmerService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Farmer> update(@PathVariable Long id, @RequestBody Farmer farmer) {
        return ResponseEntity.ok(farmerService.updateFarmer(id, farmer));
    }

    @PostMapping
    public ResponseEntity<Farmer> create(@RequestBody Farmer farmer, Principal principal) {
        return ResponseEntity.ok(farmerService.create(farmer, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean confirm) {

        if (!confirm) {
            return ResponseEntity.status(400).body("Delete farmer ID " + id + "? Add '?confirm=true' to proceed.");
        }

        farmerService.delete(id);
        return ResponseEntity.ok("Farmer moved to trash.");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Farmer>> getTrash() {
        return ResponseEntity.ok(farmerService.getTrash());
    }
}