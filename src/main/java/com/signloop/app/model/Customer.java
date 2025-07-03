package com.signloop.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long customerId;

    private String nom;
    private String prenom;
    private LocalDate birthdate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Contract> contracts = new ArrayList<>();

    public Customer() {
    }

    public Customer(String nom, String prenom, LocalDate birthdate) {
        this.nom = nom;
        this.prenom = prenom;
        this.birthdate = birthdate;
        this.contracts = new ArrayList<>();
    }
}