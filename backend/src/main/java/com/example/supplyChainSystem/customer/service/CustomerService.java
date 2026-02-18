
package com.example.supplyChainSystem.customer.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.customer.entity.Customer;
import com.example.supplyChainSystem.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public List<Customer> getAllActive() {
        return customerRepository.findAllByDeletedAtIsNull();
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer create(Customer customer, String email) {
        if (customerRepository.existsByPhoneAndDeletedAtIsNull(customer.getPhone())) {
            throw new RuntimeException("Customer phone already exists!");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        customer.setCreatedBy(String.valueOf(user.getId()));
        return customerRepository.save(customer);
    }

    public Customer updateStatus(Long id, String status) {
        Customer customer = getById(id);
        customer.setStatus(status);
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        Customer c = getById(id);
        c.setDeletedAt(LocalDateTime.now());
        customerRepository.save(c);
    }

    // Add this to CustomerService.java
    public Customer updateCustomer(Long id, Customer updatedData) {
        Customer existingCustomer = getById(id); // Reuses the "not deleted" check logic

        // Validate phone duplicate if phone is being changed
        if (!existingCustomer.getPhone().equals(updatedData.getPhone())) {
            if (customerRepository.existsByPhoneAndDeletedAtIsNull(updatedData.getPhone())) {
                throw new RuntimeException("This phone number is already used by another customer");
            }
        }

        existingCustomer.setCustomerName(updatedData.getCustomerName());
        existingCustomer.setPhone(updatedData.getPhone());
        // status and createdBy remain unchanged

        return customerRepository.save(existingCustomer);
    }

    public List<Customer> getTrash() {
        return customerRepository.findAllByDeletedAtIsNotNull();
    }
}