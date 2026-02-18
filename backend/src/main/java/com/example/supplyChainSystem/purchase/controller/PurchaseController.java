package com.example.supplyChainSystem.purchase.controller;

import com.example.supplyChainSystem.purchase.entity.Purchase;
import com.example.supplyChainSystem.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Purchase> create(@RequestBody Purchase purchase, Principal principal) {
        return ResponseEntity.ok(purchaseService.create(purchase, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAll() {
        return ResponseEntity.ok(purchaseService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> update(@PathVariable Long id, @RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.update(id, purchase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.status(400).body("Delete purchase record ID " + id + "? Add '?confirm=true' to proceed.");
        }
        purchaseService.delete(id);
        return ResponseEntity.ok("Purchase record moved to trash.");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Purchase>> getTrash() {
        return ResponseEntity.ok(purchaseService.getTrash());
    }
}