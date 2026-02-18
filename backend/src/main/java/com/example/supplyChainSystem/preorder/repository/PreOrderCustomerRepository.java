package com.example.supplyChainSystem.preorder.repository;

import com.example.supplyChainSystem.preorder.entity.PreOrderCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreOrderCustomerRepository extends JpaRepository<PreOrderCustomer, Long> {
    List<PreOrderCustomer> findAllByDeletedAtIsNull();
    List<PreOrderCustomer> findAllByDeletedAtIsNotNull();
}
