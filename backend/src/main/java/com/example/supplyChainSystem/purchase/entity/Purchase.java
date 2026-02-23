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

    @Column(name = "seller_type", nullable = false)
    private String sellerType; // e.g., "Farmer", "Supplier"

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "buy_price_per_unit", nullable = false)
    private Double buyPricePerUnit;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status = PurchaseStatus.PENDING;

    @Column(name = "date")
    private LocalDate date;

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
        // Calculate total amount automatically
        if (quantity != null && buyPricePerUnit != null) {
            totalAmount = quantity * buyPricePerUnit;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (quantity != null && buyPricePerUnit != null) {
            totalAmount = quantity * buyPricePerUnit;
        }
    }
}