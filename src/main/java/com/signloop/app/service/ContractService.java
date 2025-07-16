package com.signloop.app.service;

import com.signloop.app.model.Contract;
import com.signloop.app.model.Customer;
import com.signloop.app.repository.ContractRepository;
import com.signloop.app.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private com.signloop.app.repository.UserRepository userRepository;

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
        System.out.println("Saved contract with customer: " + saved.getCustomer());
        return saved;
    }

//    public Contract updateContract(Long id, Contract contract) {
//        return contractRepository.findById(id)
//                .map(existingContract -> {
//                    existingContract.setType(contract.getType());
//                    existingContract.setCreationDate(contract.getCreationDate());
//                    existingContract.setPaymentMode(contract.getPaymentMode());
//                    if (contract.getCustomer() != null) {
//                        existingContract.setCustomer(contract.getCustomer());
//                    }
//
//                    return contractRepository.save(existingContract);
//                })
//                .orElseThrow(() -> new RuntimeException("Contract not found"));
//    }

//    public Contract updateContract(Long id, Contract contract) {
//        return contractRepository.findById(id)
//                .map(existingContract -> {
//                    existingContract.setType(contract.getType());
//                    existingContract.setCreationDate(contract.getCreationDate());
//                    existingContract.setPaymentMode(contract.getPaymentMode());
//
//                    if (contract.getCustomer() != null) {
//                        existingContract.setCustomer(contract.getCustomer());
//                    }
//
//                    if (contract.getCinPic() != null) {
//                        // Nouveau contenu image (Base64)
//                        existingContract.setCinPic(Base64.getDecoder().decode(contract.getCinPic()));
//                    } else {
//                        // ATTENTION:
//                        // Ici tu dois décider ce que tu veux faire
//                        // Si tu veux supprimer l’image quand tu reçois "cinPic": null, active cette ligne:
//                        existingContract.setCinPic(null);
//
//                        // Si tu préfères NE PAS changer l’image quand "cinPic": null,
//                        // alors COMENTE cette ligne ci-dessus
//                    }
//
//                    return contractRepository.save(existingContract);
//                })
//                .orElseThrow(() -> new RuntimeException("Contract not found"));
//    }

    public Contract updateContract(Long id, Contract contract) {
        return contractRepository.findById(id)
                .map(existingContract -> {
                    existingContract.setType(contract.getType());
                    existingContract.setCreationDate(contract.getCreationDate());
                    existingContract.setPaymentMode(contract.getPaymentMode());

                    if (contract.getCustomer() != null) {
                        existingContract.setCustomer(contract.getCustomer());
                    }

//                    if (contract.getCinPic() != null && contract.getCinPic().length > 0) {
//                        try {
//                            byte[] decoded = Base64.getDecoder().decode(contract.getCinPic());
//                            existingContract.setCinPic(decoded);
//                        } catch (IllegalArgumentException e) {
//                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'image envoyée n'est pas valide en Base64.");
//                        }
//                    }
                    if (contract.getCinPic() != null && contract.getCinPic().length > 0) {
                        existingContract.setCinPic(contract.getCinPic());
                    } else {
                        existingContract.setCinPic(null);
                    }

//                    else {
//                        existingContract.setCinPic(null);
//                    }

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


    public Contract saveContractWithCreator(Contract contract, Long userId) {
        if (contract.getCustomer() == null) {
            throw new RuntimeException("Customer is required for a contract");
        }
        if (userId == null) {
            throw new RuntimeException("User ID is required");
        }

        // On récupère le User
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // On set le User dans le contrat
        contract.setCreatedBy(user);

        Contract saved = contractRepository.save(contract);
        System.out.println("Saved contract with creator: " + user.getEmail());
        return saved;
    }



    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                .getUserId();
    }

}