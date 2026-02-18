package com.example.supplyChainSystem.preorder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pre_orders_customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreOrderCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "item_id")
    private Long itemId;

    private Double quantity;

    @Column(name = "date_of_order")
    private LocalDate dateOfOrder;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    private String status = "pending"; // pending, confirmed, delivered, cancelled

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