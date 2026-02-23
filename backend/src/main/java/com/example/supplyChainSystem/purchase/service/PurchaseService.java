package com.example.supplyChainSystem.purchase.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.purchase.entity.Purchase;
import com.example.supplyChainSystem.purchase.entity.PurchaseStatus;
import com.example.supplyChainSystem.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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

        purchase.setCreatedBy(user.getId());

        if (purchase.getStatus() == null) {
            purchase.setStatus(PurchaseStatus.PENDING);
        }

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
        existing.setStatus(data.getStatus());

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
}