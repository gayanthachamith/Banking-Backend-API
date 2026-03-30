package com.example.bankingbackendapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String type;
    private double amount;
    private String status;
    private String fromAccount;
    private String toAccount;
}
