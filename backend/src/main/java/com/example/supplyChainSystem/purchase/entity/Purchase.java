package com.example.supplyChainSystem.purchase.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_type")
    private String sellerType; // e.g., "Farmer", "Arot", "Supplier"

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "item_id")
    private Long itemId;

    private Double quantity;

    @Column(name = "buy_price_per_unit")
    private Double buyPricePerUnit;

    private LocalDate date; // The date the purchase happened

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}