package com.example.supplyChainSystem.shopkeeper.controller;

import com.example.supplyChainSystem.shopkeeper.entity.Shopkeeper;
import com.example.supplyChainSystem.shopkeeper.service.ShopkeeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/shopkeepers")
@RequiredArgsConstructor
public class ShopkeeperController {
    private final ShopkeeperService shopkeeperService;

    @PostMapping
    public ResponseEntity<Shopkeeper> create(@RequestBody Shopkeeper shopkeeper, Principal principal) {
        return ResponseEntity.ok(shopkeeperService.create(shopkeeper, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Shopkeeper>> getAll() {
        return ResponseEntity.ok(shopkeeperService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shopkeeper> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shopkeeperService.getById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Shopkeeper> update(@PathVariable Long id, @RequestBody Shopkeeper shopkeeper) {
        return ResponseEntity.ok(shopkeeperService.updateShopkeeper(id, shopkeeper));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.status(400).body("Delete shopkeeper ID " + id + "? Add '?confirm=true' to proceed.");
        }
        shopkeeperService.delete(id);
        return ResponseEntity.ok("Shopkeeper soft-deleted successfully");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Shopkeeper>> getTrash() {
        return ResponseEntity.ok(shopkeeperService.getTrash());
    }
}