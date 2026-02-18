package com.example.supplyChainSystem.supplier.service;


import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
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

    public List<Supplier> getAllActive() {
        return supplierRepository.findAllByDeletedAtIsNull();
    }

    public Supplier getById(Long id) {
        return supplierRepository.findById(id)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public Supplier create(Supplier supplier, String email) {
        if (supplierRepository.existsByPhoneAndDeletedAtIsNull(supplier.getPhone())) {
            throw new RuntimeException("Phone number already exists!");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        supplier.setCreatedBy(String.valueOf(user.getId()));
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier data) {
        Supplier existing = getById(id);
        existing.setName(data.getName());
        existing.setPhone(data.getPhone());
        existing.setAddress(data.getAddress());
        return supplierRepository.save(existing);
    }

    public Supplier updateStatus(Long id, String status) {
        Supplier supplier = getById(id);
        supplier.setStatus(status);
        return supplierRepository.save(supplier);
    }

    public void delete(Long id) {
        Supplier s = getById(id);
        s.setDeletedAt(LocalDateTime.now());
        supplierRepository.save(s);
    }

    public List<Supplier> getTrash() {
        return supplierRepository.findAllByDeletedAtIsNotNull();
    }
}
