package com.example.bankingbackendapi.dto;


import lombok.Data;

@Data
public class AccountRequest {
    private String accountNumber;
    private Double initialBalance;
    private Long userId;
}