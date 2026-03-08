package com.example.supplyChainSystem.supplier.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.redis.RedisService;
import com.example.supplyChainSystem.supplier.entity.Supplier;
import com.example.supplyChainSystem.supplier.repository.SupplierRepository;
//import com.example.supplyChainSystem.user.entity.User;
//import com.example.supplyChainSystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final RedisService redisService; // inject Redis

    private static final long CACHE_TIMEOUT = 300; // 5 minutes

    // ---------------- Active suppliers list ----------------
    public List<Supplier> getAllActive() {
        // Check Redis first
        Object cached = redisService.getValue("suppliers:active");
        if (cached != null) {
            return (List<Supplier>) cached;
        }

        List<Supplier> suppliers = supplierRepository.findAllByDeletedAtIsNull();
        redisService.saveValue("suppliers:active", suppliers, CACHE_TIMEOUT);
        return suppliers;
    }

    // ---------------- Single supplier by ID ----------------
//    public Supplier getById(Long id) {
//        Object cached = redisService.getValue("supplier:" + id);
//        if (cached != null) {
//            System.out.println(">>> Supplier loaded from Redis: " + id);
//            return (Supplier) cached;
//        }
//
//        Supplier supplier = supplierRepository.findById(id)
//                .filter(s -> s.getDeletedAt() == null)
//                .orElseThrow(() -> new RuntimeException("Supplier not found"));
//
//        System.out.println(">>> Supplier loaded from Database: " + id);
//
//        // Save in Redis
//        redisService.saveValue("supplier:" + id, supplier, 300); // 5 min TTL
//        return supplier;
//    }

    public Supplier getById(Long id) {
        // Try Redis
        Object cached = redisService.getValue("supplier:" + id);
        if (cached != null) {
            System.out.println(">>> Supplier loaded from Redis: " + id);
            return (Supplier) cached;
        }

        // Load from database
        Supplier supplier = supplierRepository.findById(id)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        System.out.println(">>> Supplier loaded from Database: " + id);

        // -----------------------------
        // Convert to plain Supplier to avoid Hibernate proxy issue
        Supplier plainSupplier = new Supplier();
        plainSupplier.setId(supplier.getId());
        plainSupplier.setName(supplier.getName());
        plainSupplier.setPhone(supplier.getPhone());
        plainSupplier.setAddress(supplier.getAddress());
        plainSupplier.setStatus(supplier.getStatus());
        // You can also copy other fields if needed

        // Save to Redis
        redisService.saveValue("supplier:" + id, plainSupplier, CACHE_TIMEOUT);
        // -----------------------------

        return plainSupplier;
    }

    // ---------------- Create supplier ----------------
    public Supplier create(Supplier supplier, String email) {
        if (supplierRepository.existsByPhoneAndDeletedAtIsNull(supplier.getPhone())) {
            throw new RuntimeException("Phone number already exists!");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        supplier.setCreatedBy(String.valueOf(user.getId()));
        Supplier saved = supplierRepository.save(supplier);

        // Invalidate cache
        redisService.deleteValue("suppliers:active");
        redisService.saveValue("supplier:" + saved.getId(), saved, CACHE_TIMEOUT);

        return saved;
    }

    // ---------------- Update supplier ----------------
    public Supplier updateSupplier(Long id, Supplier data) {
        Supplier existing = getById(id); // already cached
        existing.setName(data.getName());
        existing.setPhone(data.getPhone());
        existing.setAddress(data.getAddress());

        Supplier updated = supplierRepository.save(existing);

        // Update Redis cache
        redisService.saveValue("supplier:" + id, updated, CACHE_TIMEOUT);
        redisService.deleteValue("suppliers:active"); // invalidate list cache
        return updated;
    }

    // ---------------- Update status ----------------
    public Supplier updateStatus(Long id, String status) {
        Supplier supplier = getById(id);
        supplier.setStatus(status);
        Supplier updated = supplierRepository.save(supplier);

        redisService.saveValue("supplier:" + id, updated, CACHE_TIMEOUT);
        redisService.deleteValue("suppliers:active");
        return updated;
    }

    // ---------------- Delete supplier ----------------
    public void delete(Long id) {
        Supplier s = getById(id);
        s.setDeletedAt(LocalDateTime.now());
        supplierRepository.save(s);

        redisService.deleteValue("supplier:" + id);
        redisService.deleteValue("suppliers:active");
    }

    // ---------------- Trash list ----------------
    public List<Supplier> getTrash() {
        return supplierRepository.findAllByDeletedAtIsNotNull();
    }
}
