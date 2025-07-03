package com.signloop.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long contractId;

    private String type;
    private LocalDate creationDate;
    private String paymentMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)

    @JsonIgnore
    private Customer customer;

    public Contract() {
    }

    public Contract(String type, LocalDate creationDate, String paymentMode) {
        this.type = type;
        this.creationDate = creationDate;
        this.paymentMode = paymentMode;
    }
}