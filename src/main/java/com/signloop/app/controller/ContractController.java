//package com.signloop.app.controller;
//
//import com.signloop.app.model.Contract;
//import com.signloop.app.service.ContractService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/contracts")
//public class ContractController {
//
//    @Autowired
//    private ContractService contractService;
//
//    @Operation(summary = "Get All contracts", description = "Returns a list of all contracts")
//    @ApiResponse(responseCode = "200", description = "successful operation")
//    @GetMapping
//    public List<Contract> getAllContracts() {
//        return contractService.getAllContracts();
//    }
//
//    @Operation(summary = "Get contract by ID", description = "Returns a contract by its ID")
//    @ApiResponse(responseCode = "200", description = "Successful operation")
//    @ApiResponse(responseCode = "404", description = "Contract not found")
//    @GetMapping("/{id}")
//    public Contract getContractById(@PathVariable Long id) {
//        return contractService.getContractById(id)
//                .orElseThrow(() -> new RuntimeException("Contract not found"));
//    }
//
//    @Operation(summary = "Create a new contract", description = "Adds a new contract")
//    @ApiResponse(responseCode = "200", description = "Contract created successfully")
//    @PostMapping
//    public Contract createContract(@RequestBody Contract contract) {
//        return contractService.saveContract(contract);
//    }
//
//    @Operation(summary = "Update a contract", description = "Updates contract information")
//    @ApiResponse(responseCode = "200", description = "Contract updated successfully.")
//    @PutMapping("/{id}")
//    public Contract updateContract(@PathVariable Long id, @RequestBody Contract contract) {
//        return contractService.updateContract(id, contract);
//    }
//
//    @Operation(summary = "Delete a contract", description = "Deletes a contract by ID")
//    @ApiResponse(responseCode = "200", description = "Contract deleted successfully.")
//    @DeleteMapping("/{id}")
//    public void deleteContract(@PathVariable Long id) {
//        contractService.deleteContract(id);
//    }
//
//}

package com.signloop.app.controller;

import com.signloop.app.model.Contract;
import com.signloop.app.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Operation(summary = "Get All contracts", description = "Returns a list of all contracts")
    @ApiResponse(responseCode = "200", description = "successful operation")
    @GetMapping
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    @Operation(summary = "Get contract by ID", description = "Returns a contract by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Contract not found")
    @GetMapping("/{id}")
    public Contract getContractById(@PathVariable Long id) {
        return contractService.getContractById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    @Operation(summary = "Create a new contract", description = "Adds a new contract")
    @ApiResponse(responseCode = "200", description = "Contract created successfully")
    @PostMapping
    public Contract createContract(@RequestBody Contract contract) {
        Contract savedContract = contractService.saveContract(contract);
        // Assurer que customer est inclus dans la réponse
        return savedContract;
    }

    @Operation(summary = "Update a contract", description = "Updates contract information")
    @ApiResponse(responseCode = "200", description = "Contract updated successfully.")
    @PutMapping("/{id}")
    public Contract updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        return contractService.updateContract(id, contract);
    }

    @Operation(summary = "Delete a contract", description = "Deletes a contract by ID")
    @ApiResponse(responseCode = "200", description = "Contract deleted successfully.")
    @DeleteMapping("/{id}")
    public void deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);

    }
}
