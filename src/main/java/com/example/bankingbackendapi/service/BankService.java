package com.example.bankingbackendapi.service;


import com.example.bankingbackendapi.entity.Account;
import com.example.bankingbackendapi.entity.Transaction;
import com.example.bankingbackendapi.repository.AccountRepository;
import com.example.bankingbackendapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BankService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

//    deposit
    @Transactional
    public Transaction deposit(String accountNumber, double amount){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setToAccount(account);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);

    }

//     withdraw
    @Transactional
    public Transaction withdraw(String accountNumber, double amount){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found"));

        if(account.getBalance()< amount){
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance()- amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setToAccount(account);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);



    }

//transfer

    @Transactional
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setType("TRANSFER");
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);
    }

}
