package com.signloop.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String telephone;
    private String adresse;

    private String role;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @OneToMany(mappedBy = "createdBy")
    //@JsonIgnore
    @JsonManagedReference(value = "user-contract")
    private List<Contract> contracts;
}
