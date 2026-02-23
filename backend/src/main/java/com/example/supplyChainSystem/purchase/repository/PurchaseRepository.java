package com.example.supplyChainSystem.purchase.repository;

import com.example.supplyChainSystem.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByDeletedAtIsNull();
    List<Purchase> findAllByDeletedAtIsNotNull();
}