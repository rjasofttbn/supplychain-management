package com.example.supplyChainSystem.shopkeeper.repository;

import com.example.supplyChainSystem.shopkeeper.entity.Shopkeeper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShopkeeperRepository extends JpaRepository<Shopkeeper, Long> {
    List<Shopkeeper> findAllByDeletedAtIsNull();
    List<Shopkeeper> findAllByDeletedAtIsNotNull();
    boolean existsByPhoneAndDeletedAtIsNull(String phone);
}