package com.example.supplyChainSystem.arot.controller;

import com.example.supplyChainSystem.arot.entity.Arot;
import com.example.supplyChainSystem.arot.service.ArotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/arots")
@RequiredArgsConstructor
public class ArotController {
    private final ArotService arotService;

    @PostMapping
    public ResponseEntity<Arot> create(@RequestBody Arot arot, Principal principal) {
        return ResponseEntity.ok(arotService.create(arot, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Arot>> getAll() {
        return ResponseEntity.ok(arotService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arot> getById(@PathVariable Long id) {
        return ResponseEntity.ok(arotService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Arot> update(@PathVariable Long id, @RequestBody Arot arot) {
        return ResponseEntity.ok(arotService.updateArot(id, arot));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Arot> setStatusActive(@PathVariable Long id) {
        return ResponseEntity.ok(arotService.updateStatus(id, "active"));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Arot> setStatusInactive(@PathVariable Long id) {
        return ResponseEntity.ok(arotService.updateStatus(id, "inactive"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.status(400).body("Delete Arot ID " + id + "? Add '?confirm=true' to proceed.");
        }
        arotService.delete(id);
        return ResponseEntity.ok("Arot moved to trash.");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Arot>> getTrash() {
        return ResponseEntity.ok(arotService.getTrash());
    }
}