package com.example.supplyChainSystem.item.service;

import com.example.supplyChainSystem.item.entity.Item;
import com.example.supplyChainSystem.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAllByDeletedAtIsNull();
    }


    public List<Item> getTrashBin() {
        return itemRepository.findAllByDeletedAtIsNotNull();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .filter(item -> item.getDeletedAt() == null) // Ensure we don't return soft-deleted items
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    public Item createItem(Item item, String username) {
        // 1. Check for duplicate name
        if (itemRepository.existsByName(item.getName())) {
            throw new RuntimeException("An item with this name already exists.");
        }

        // 2. Check for duplicate name_bn
        if (itemRepository.existsByNameBn(item.getNameBn())) {
            throw new RuntimeException("এই নামে একটি পণ্য ইতিমধ্যে বিদ্যমান। (Item with this Bengali name already exists.)");
        }

        item.setCreatedBy(username);
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.setName(itemDetails.getName());
        item.setNameBn(itemDetails.getNameBn());
        item.setUnit(itemDetails.getUnit());
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.setDeletedAt(LocalDateTime.now()); // <--- This is the Soft Delete "Mark"
        itemRepository.save(item);               // <--- You are UPDATING, not DELETING
    }

}
