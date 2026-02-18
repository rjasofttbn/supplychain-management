package com.example.supplyChainSystem.arot.repository;

import com.example.supplyChainSystem.arot.entity.Arot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArotRepository extends JpaRepository<Arot, Long> {
    List<Arot> findAllByDeletedAtIsNull();
    List<Arot> findAllByDeletedAtIsNotNull();
    boolean existsByPhoneAndDeletedAtIsNull(String phone);
}
