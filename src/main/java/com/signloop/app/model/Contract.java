//package com.signloop.app.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//@Entity
//@Table(name = "contract")
//public class Contract {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long contractId;
//
//    private String type;
//    private LocalDate creationDate;
//    private String paymentMode;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id", nullable = false)
//    @JsonIgnore
//    private Customer customer;
//
//    @JsonProperty("customerId")
//    public Long getCustomerId() {
//        return customer != null ? customer.getCustomerId() : null;
//    }
//
//    public Contract() {
//    }
//
//    public Contract(String type, LocalDate creationDate, String paymentMode) {
//        this.type = type;
//        this.creationDate = creationDate;
//        this.paymentMode = paymentMode;
//    }
//}

package com.signloop.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    private String type;
    private LocalDate creationDate;
    private String paymentMode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User createdBy;

    @Lob
    @Column(name = "cin_pic")
    private byte[] cinPic;

    @JsonProperty("customerId")
    public Long getCustomerId() {
        return customer != null ? customer.getCustomerId() : null;
    }

    public Contract() {
    }

    public Contract(String type, LocalDate creationDate, String paymentMode) {
        this.type = type;
        this.creationDate = creationDate;
        this.paymentMode = paymentMode;
    }
}