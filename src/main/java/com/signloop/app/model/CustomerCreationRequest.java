package com.signloop.app.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerCreationRequest {
    private String nom;
    private String prenom;
    private LocalDate birthdate;
    private List<ContractCreationRequest> contracts;
}
