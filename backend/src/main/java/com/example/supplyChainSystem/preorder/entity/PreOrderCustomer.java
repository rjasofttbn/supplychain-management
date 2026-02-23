package com.example.supplyChainSystem.preorder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.supplyChainSystem.common.enums.PreOrderStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "pre_order_customers")
@Data // <--- THIS GENERATES THE MISSING SETTERS AND GETTERS
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreOrderStatus status = PreOrderStatus.PENDING;

    @Column(name = "created_by")
    private Long createdBy;

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