package com.example.supplyChainSystem.item.repository;

import com.example.supplyChainSystem.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // You can add custom queries here, e.g., finding items not deleted
    List<Item> findAllByDeletedAtIsNull();

    List<Item> findAllByDeletedAtIsNotNull();
    boolean existsByName(String name);
    boolean existsByNameBn(String nameBn);
}
