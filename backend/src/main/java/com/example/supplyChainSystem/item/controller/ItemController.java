package com.example.supplyChainSystem.item.controller;

import com.example.supplyChainSystem.item.entity.Item;
import com.example.supplyChainSystem.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAll() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody Item item, Principal principal) {
        // principal.getName() returns the username/email extracted from the JWT
        return ResponseEntity.ok(itemService.createItem(item, principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean confirm) {

        if (!confirm) {
            return ResponseEntity.status(400).body("Are you sure you want to delete this item? Please add '?confirm=true' to your URL to proceed.");
        }

        itemService.deleteItem(id);
        return ResponseEntity.ok("Item soft-deleted successfully");
    }

    @GetMapping("/trash")
    public ResponseEntity<List<Item>> getTrash() {
        return ResponseEntity.ok(itemService.getTrashBin());
    }

}
