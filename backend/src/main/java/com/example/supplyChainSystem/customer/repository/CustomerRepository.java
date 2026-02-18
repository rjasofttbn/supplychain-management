package com.example.supplyChainSystem.customer.repository;

import com.example.supplyChainSystem.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByDeletedAtIsNull();
    List<Customer> findAllByDeletedAtIsNotNull();
    boolean existsByPhoneAndDeletedAtIsNull(String phone);
}