package com.example.supplyChainSystem.sales.controller;



import com.example.supplyChainSystem.sales.entity.Sale;
import com.example.supplyChainSystem.sales.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> create(@RequestBody Sale sale, Principal principal) {
        return ResponseEntity.ok(saleService.create(sale, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAll() {
        return ResponseEntity.ok(saleService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> update(@PathVariable Long id, @RequestBody Sale sale) {
        return ResponseEntity.ok(saleService.update(id, sale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return ResponseEntity.status(400).body("Delete sale record ID " + id + "? Add '?confirm=true' to proceed.");
        }
        saleService.delete(id);
        return ResponseEntity.ok("Sale record moved to trash.");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Sale>> getTrash() {
        return ResponseEntity.ok(saleService.getTrash());
    }
    //    demo /test data
    //{
    //    "buyerType": "Shopkeeper",
    //        "itemId": 3,
    //        "quantity": 50.5,
    //        "sellPricePerUnit": 60.0,
    //        "date": "2026-02-17"
    //}
}
