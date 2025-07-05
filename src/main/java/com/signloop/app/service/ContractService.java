package com.signloop.app.service;

import com.signloop.app.model.Contract;
import com.signloop.app.repository.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {
    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract saveContract(Contract contract) {
        return contractRepository.save(contract);
    }
    public Contract updateContract(Long id, Contract contractRequest) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (contractRequest.getType() != null) {
            existingContract.setType(contractRequest.getType());
        }

        if (contractRequest.getCreationDate() != null) {
            existingContract.setCreationDate(contractRequest.getCreationDate());
        }
        if (contractRequest.getPaymentMode() != null) {
            existingContract.setPaymentMode(contractRequest.getPaymentMode());
        }

        return contractRepository.save(existingContract);
    }

    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new RuntimeException("Contract not found");
        }
        contractRepository.deleteById(id);
    }
}
