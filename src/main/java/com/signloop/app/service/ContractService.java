package com.signloop.app.service;

import com.signloop.app.model.Contract;
import com.signloop.app.model.Customer;
import com.signloop.app.repository.ContractRepository;
import com.signloop.app.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract saveContract(Contract contract) {
        if (contract.getCustomer() == null) {
            throw new RuntimeException("Customer is required for a contract");
        }
        Contract saved = contractRepository.save(contract);
        System.out.println("Saved contract with customer: " + saved.getCustomer()); // Débogage
        return saved;
    }

    public Contract updateContract(Long id, Contract contract) {
        return contractRepository.findById(id)
                .map(existingContract -> {
                    existingContract.setType(contract.getType());
                    existingContract.setCreationDate(contract.getCreationDate());
                    existingContract.setPaymentMode(contract.getPaymentMode());
                    if (contract.getCustomer() != null) {
                        existingContract.setCustomer(contract.getCustomer());
                    }
                    return contractRepository.save(existingContract);
                })
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public void deleteContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        Customer customer = contract.getCustomer();
        if (customer != null) {
            customer.getContracts().remove(contract);
            customerRepository.save(customer);
        } else {

            contractRepository.delete(contract);
        }
    }
}