package com.example.supplyChainSystem.purchase.repository;

import com.example.supplyChainSystem.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByDeletedAtIsNull();
    List<Purchase> findAllByDeletedAtIsNotNull();
}
