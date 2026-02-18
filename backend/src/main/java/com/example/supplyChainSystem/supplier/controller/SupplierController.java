package com.example.supplyChainSystem.supplier.controller;


import com.example.supplyChainSystem.supplier.entity.Supplier;
import com.example.supplyChainSystem.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<Supplier> create(@RequestBody Supplier supplier, Principal principal) {
        return ResponseEntity.ok(supplierService.create(supplier, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAll() {
        return ResponseEntity.ok(supplierService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable Long id, @RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplier));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Supplier> setStatusActive(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.updateStatus(id, "active"));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Supplier> setStatusInactive(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.updateStatus(id, "inactive"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.status(400).body("Delete supplier ID " + id + "? Add '?confirm=true' to proceed.");
        }
        supplierService.delete(id);
        return ResponseEntity.ok("Supplier moved to trash.");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Supplier>> getTrash() {
        return ResponseEntity.ok(supplierService.getTrash());
    }
}
