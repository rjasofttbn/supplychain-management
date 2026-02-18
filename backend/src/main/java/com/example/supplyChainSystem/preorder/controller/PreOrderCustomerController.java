package com.example.supplyChainSystem.preorder.controller;

import com.example.supplyChainSystem.preorder.entity.PreOrderCustomer;
import com.example.supplyChainSystem.preorder.service.PreOrderCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pre-orders-customer")
@RequiredArgsConstructor
public class PreOrderCustomerController {
    private final PreOrderCustomerService service;

    @PostMapping
    public ResponseEntity<PreOrderCustomer> create(@RequestBody PreOrderCustomer order, Principal principal) {
        return ResponseEntity.ok(service.create(order, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<PreOrderCustomer>> getAll() {
        return ResponseEntity.ok(service.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreOrderCustomer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreOrderCustomer> update(@PathVariable Long id, @RequestBody PreOrderCustomer order) {
        return ResponseEntity.ok(service.update(id, order));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PreOrderCustomer> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) return ResponseEntity.status(400).body("Confirm deletion with ?confirm=true");
        service.delete(id);
        return ResponseEntity.ok("Pre-Order moved to trash.");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<PreOrderCustomer>> getTrash() {
        return ResponseEntity.ok(service.getTrash());
    }
    //    demo data
    //{
    //    "customerId": 1,
    //        "itemId": 10,
    //        "quantity": 25.0,
    //        "dateOfOrder": "2026-02-17",
    //        "deliveryDate": "2026-02-25",
    //        "status": "pending"
    //}
}
