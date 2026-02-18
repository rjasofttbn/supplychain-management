package com.example.supplyChainSystem.purchase.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.purchase.entity.Purchase;
import com.example.supplyChainSystem.purchase.repository.PurchaseRepository;
//import com.example.supplyChainSystem.user.entity.User;
//import com.example.supplyChainSystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    public List<Purchase> getAllActive() {
        return purchaseRepository.findAllByDeletedAtIsNull();
    }

    public Purchase getById(Long id) {
        return purchaseRepository.findById(id)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Purchase record not found"));
    }

    public Purchase create(Purchase purchase, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        purchase.setCreatedBy(String.valueOf(user.getId()));
        return purchaseRepository.save(purchase);
    }

    public Purchase update(Long id, Purchase data) {
        Purchase existing = getById(id);
        existing.setSellerType(data.getSellerType());
        existing.setSellerId(data.getSellerId());
        existing.setItemId(data.getItemId());
        existing.setQuantity(data.getQuantity());
        existing.setBuyPricePerUnit(data.getBuyPricePerUnit());
        existing.setDate(data.getDate());
        return purchaseRepository.save(existing);
    }

    public void delete(Long id) {
        Purchase p = getById(id);
        p.setDeletedAt(LocalDateTime.now());
        purchaseRepository.save(p);
    }

    public List<Purchase> getTrash() {
        return purchaseRepository.findAllByDeletedAtIsNotNull();
    }
        //test data
        //    {
        //        "sellerType": "Farmer",
        //            "sellerId": 1,
        //            "itemId": 5,
        //            "quantity": 100.0,
        //            "buyPricePerUnit": 45.5,
        //            "date": "2026-02-17"
        //    }
}