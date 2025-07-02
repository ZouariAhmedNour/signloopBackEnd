package com.signloop.app.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractCreationRequest {
    private String type;
    private LocalDate creationDate;
    private String paymentMode;
}
