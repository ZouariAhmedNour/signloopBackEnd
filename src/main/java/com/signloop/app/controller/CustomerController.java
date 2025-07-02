package com.signloop.app.controller;


import com.signloop.app.model.Contract;
import com.signloop.app.model.Customer;
import com.signloop.app.model.CustomerCreationRequest;
import com.signloop.app.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customers", description = "Returns a listt of all customers")
    @ApiResponse(responseCode = "200", description = "successful operation")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(summary = "Get customer by ID", description = "Returns a customer by their ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Operation(summary = "Create a new customer", description = "Adds a new customer")
    @ApiResponse(responseCode = "200", description = "Customer created successfully.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Customer createCustomer(@RequestBody CustomerCreationRequest request) {

        Customer customer = new Customer();
        customer.setNom(request.getNom());
        customer.setPrenom(request.getPrenom());
        customer.setBirthdate(request.getBirthdate());

        if (request.getContracts() != null) {
            List<Contract> contracts = request.getContracts().stream()
                    .map(c -> {
                        Contract contract = new Contract();
                        contract.setType(c.getType());
                        contract.setCreationDate(c.getCreationDate());
                        contract.setPaymentMode(c.getPaymentMode());
                        contract.setCustomer(customer); // Important : lien parent
                        return contract;
                    })
                    .toList();

            customer.setContracts(contracts);
        }

        return customerService.saveCustomer(customer);
    }

}
