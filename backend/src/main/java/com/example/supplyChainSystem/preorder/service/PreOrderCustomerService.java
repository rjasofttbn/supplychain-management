package com.example.supplyChainSystem.preorder.service;


import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.preorder.entity.PreOrderCustomer;
import com.example.supplyChainSystem.preorder.repository.PreOrderCustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreOrderCustomerService {
    private final PreOrderCustomerRepository repository;
    private final UserRepository userRepository;

    public List<PreOrderCustomer> getAllActive() {
        return repository.findAllByDeletedAtIsNull();
    }

    public PreOrderCustomer getById(Long id) {
        return repository.findById(id)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Pre-Order not found"));
    }

    public PreOrderCustomer create(PreOrderCustomer order, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setCreatedBy(String.valueOf(user.getId()));
        return repository.save(order);
    }

    public PreOrderCustomer update(Long id, PreOrderCustomer data) {
        PreOrderCustomer existing = getById(id);
        existing.setCustomerId(data.getCustomerId());
        existing.setItemId(data.getItemId());
        existing.setQuantity(data.getQuantity());
        existing.setDateOfOrder(data.getDateOfOrder());
        existing.setDeliveryDate(data.getDeliveryDate());
        existing.setStatus(data.getStatus());
        return repository.save(existing);
    }

    public PreOrderCustomer updateStatus(Long id, String status) {
        PreOrderCustomer order = getById(id);
        order.setStatus(status);
        return repository.save(order);
    }

    public void delete(Long id) {
        PreOrderCustomer order = getById(id);
        order.setDeletedAt(LocalDateTime.now());
        repository.save(order);
    }

    public List<PreOrderCustomer> getTrash() {
        return repository.findAllByDeletedAtIsNotNull();
    }
}
