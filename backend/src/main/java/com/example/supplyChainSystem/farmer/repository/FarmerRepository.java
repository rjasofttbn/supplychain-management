package com.example.supplyChainSystem.farmer.repository;

import com.example.supplyChainSystem.farmer.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    List<Farmer> findAllByDeletedAtIsNull();
    List<Farmer> findAllByDeletedAtIsNotNull();

    boolean existsByPhoneAndDeletedAtIsNull(String phone);
//    boolean existsByPhoneAndDeletedAtIsNull(String phone);
}

