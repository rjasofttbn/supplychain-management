package com.example.supplyChainSystem.sales.service;


import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;

import com.example.supplyChainSystem.sales.entity.Sale;
import com.example.supplyChainSystem.sales.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;

    public List<Sale> getAllActive() {
        return saleRepository.findAllByDeletedAtIsNull();
    }

    public Sale getById(Long id) {
        return saleRepository.findById(id)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Sale record not found"));
    }

    public Sale create(Sale sale, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        sale.setCreatedBy(String.valueOf(user.getId()));
        return saleRepository.save(sale);
    }

    public Sale update(Long id, Sale data) {
        Sale existing = getById(id);
        existing.setBuyerType(data.getBuyerType());
        existing.setItemId(data.getItemId());
        existing.setQuantity(data.getQuantity());
        existing.setSellPricePerUnit(data.getSellPricePerUnit());
        existing.setDate(data.getDate());
        return saleRepository.save(existing);
    }

    public void delete(Long id) {
        Sale s = getById(id);
        s.setDeletedAt(LocalDateTime.now());
        saleRepository.save(s);
    }

    public List<Sale> getTrash() {
        return saleRepository.findAllByDeletedAtIsNotNull();
    }
}
