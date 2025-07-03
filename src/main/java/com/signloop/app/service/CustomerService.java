package com.signloop.app.service;


import com.signloop.app.model.Customer;
import com.signloop.app.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerRequest) {
        Customer existingCustomer = customerRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customerRequest.getNom() != null) {
            existingCustomer.setNom(customerRequest.getNom());
        }

        if (customerRequest.getPrenom() != null) {
            existingCustomer.setPrenom(customerRequest.getPrenom());
        }

        if (customerRequest.getBirthdate() != null) {
            existingCustomer.setBirthdate(customerRequest.getBirthdate());
        }

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
