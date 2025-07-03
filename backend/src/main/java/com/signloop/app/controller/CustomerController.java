package com.signloop.app.controller;


import com.signloop.app.model.Customer;
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

    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
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
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        // IMPORTANT: s'assurer que tous les contrats pointent vers ce customer
        if (customer.getContracts() != null) {
            customer.getContracts().forEach(contract -> contract.setCustomer(customer));
        }
        return customerService.saveCustomer(customer);
    }


    @Operation(summary = "Update a customer", description = "Updates customer information")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully.")
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Delete a customer", description = "Deletes a customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer deleted successfully.")
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @RestController
    @RequestMapping("/api/test")
    public class TestController {

        @PostMapping
        public String test(@RequestBody String body) {
            return "OK: " + body;
        }
    }










}
