package com.example.supplyChainSystem.customer.controller;

import com.example.supplyChainSystem.customer.entity.Customer;
import com.example.supplyChainSystem.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer, Principal principal) {
        return ResponseEntity.ok(customerService.create(customer, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.getAllActive());
    }

    // 1. GET ID WISE DATA
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    // 2. UPDATE CUSTOMER ID WISE
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Customer> setStatusActive(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.updateStatus(id, "active"));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Customer> setStatusInactive(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.updateStatus(id, "inactive"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.status(400).body("Delete customer ID " + id + "? Add '?confirm=true' to proceed.");
        }
        customerService.delete(id);
        return ResponseEntity.ok("Customer soft-deleted successfully");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Customer>> getTrash() {
        return ResponseEntity.ok(customerService.getTrash());
    }
}
