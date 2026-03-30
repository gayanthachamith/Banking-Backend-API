package com.example.bankingbackendapi.controller;


import com.example.bankingbackendapi.dto.AccountRequest;
import com.example.bankingbackendapi.dto.TransactionResponse;
import com.example.bankingbackendapi.entity.Account;
import com.example.bankingbackendapi.entity.Transaction;
import com.example.bankingbackendapi.service.BankService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    // ========================
    // Deposit
    // ========================
    @PostMapping("/deposit")
    public TransactionResponse deposit(@RequestParam String accountNumber,
                                       @RequestParam double amount) {
        return bankService.deposit(accountNumber, amount);
    }

    // ========================
    // Withdraw
    // ========================
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@RequestParam String accountNumber,
                                @RequestParam double amount) {
        return bankService.withdraw(accountNumber, amount);
    }

    // ========================
    // Transfer
    // ========================
    @PostMapping("/transfer")
    public TransactionResponse transfer(@RequestParam String fromAccount,
                                @RequestParam String toAccount,
                                @RequestParam double amount) {
        return bankService.transfer(fromAccount, toAccount, amount);
    }

    @GetMapping("/test-db")
    public String testDB(){
        return "Connected";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Working!";  //for the testing perpose
    }

    @PostMapping("/create-account")
    public Account createAccount(@RequestBody AccountRequest request) {
        return bankService.createAccount(request);
    }
}