package com.example.supplyChainSystem.supplier.repository;


import com.example.supplyChainSystem.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findAllByDeletedAtIsNull();
    List<Supplier> findAllByDeletedAtIsNotNull();
    boolean existsByPhoneAndDeletedAtIsNull(String phone);
}
