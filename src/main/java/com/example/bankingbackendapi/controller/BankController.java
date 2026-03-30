package com.example.bankingbackendapi.controller;


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
    public Transaction deposit(@RequestParam String accountNumber,
                               @RequestParam double amount) {
        return bankService.deposit(accountNumber, amount);
    }

    // ========================
    // Withdraw
    // ========================
    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam String accountNumber,
                                @RequestParam double amount) {
        return bankService.withdraw(accountNumber, amount);
    }

    // ========================
    // Transfer
    // ========================
    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam String fromAccount,
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
        return "Hello Working!";
    }
}