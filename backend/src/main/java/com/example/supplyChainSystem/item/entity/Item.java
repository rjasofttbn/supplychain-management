package com.example.supplyChainSystem.item.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction; // Add this import

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "name_bn", unique = true, nullable = false)
    private String nameBn;

    @Column(nullable = false)
    private String unit;

    @Column(name = "sku", unique = true, nullable = false)
    private String sku;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice = 0.0;

    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice = 0.0;

    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel = 0;

    private String status = "active";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
