package com.example.supplyChainSystem.shopkeeper.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.shopkeeper.entity.Shopkeeper;
import com.example.supplyChainSystem.shopkeeper.repository.ShopkeeperRepository;
//import com.example.supplyChainSystem.user.entity.User;
//import com.example.supplyChainSystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopkeeperService {
    private final ShopkeeperRepository shopkeeperRepository;
    private final UserRepository userRepository;

    public Shopkeeper create(Shopkeeper shopkeeper, String email) {
        if (shopkeeperRepository.existsByPhoneAndDeletedAtIsNull(shopkeeper.getPhone())) {
            throw new RuntimeException("Shopkeeper phone already exists!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        shopkeeper.setCreatedBy(String.valueOf(user.getId()));
        return shopkeeperRepository.save(shopkeeper);
    }
    public List<Shopkeeper> getAllActive() {
        return shopkeeperRepository.findAllByDeletedAtIsNull();
    }

    public Shopkeeper getById(Long id) {
        return shopkeeperRepository.findById(id)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Shopkeeper not found"));
    }


    public Shopkeeper updateShopkeeper(Long id, Shopkeeper data) {
        Shopkeeper existing = getById(id);
        existing.setName(data.getName());
        existing.setPhone(data.getPhone());
        existing.setAddress(data.getAddress());
        return shopkeeperRepository.save(existing);
    }

    public void delete(Long id) {
        Shopkeeper s = getById(id);
        s.setDeletedAt(LocalDateTime.now());
        shopkeeperRepository.save(s);
    }

    public List<Shopkeeper> getTrash() {
        return shopkeeperRepository.findAllByDeletedAtIsNotNull();
    }
}