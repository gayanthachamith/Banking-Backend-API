package com.example.bankingbackendapi.service;


import com.example.bankingbackendapi.dto.AccountRequest;
import com.example.bankingbackendapi.dto.TransactionResponse;
import com.example.bankingbackendapi.entity.Account;
import com.example.bankingbackendapi.entity.Transaction;
import com.example.bankingbackendapi.entity.User;
import com.example.bankingbackendapi.exception.ResourceNotFoundException;
import com.example.bankingbackendapi.repository.AccountRepository;
import com.example.bankingbackendapi.repository.TransactionRepository;
import com.example.bankingbackendapi.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BankService {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository, UserRepository userRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

//    account creation
    public Account createAccount(AccountRequest request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setBalance(request.getInitialBalance());
        account.setUser(user);

        return accountRepository.save(account);
    }

//    deposit
    @Transactional
    public TransactionResponse deposit(String accountNumber, double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setType("DEPOSIT");
        txn.setAmount(amount);
        txn.setToAccount(account);
        txn.setStatus("SUCCESS");
        log.info("Deposit request: account={}, amount={}", accountNumber, amount);

        transactionRepository.save(txn);

        return new TransactionResponse(
                txn.getType(),
                txn.getAmount(),
                txn.getStatus(),
                null,
                account.getAccountNumber()
        );
    }

//     withdraw
    @Transactional
    public TransactionResponse withdraw(String accountNumber, double amount){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(account.getBalance()< amount){
            log.error("Insufficient balance for account {}", accountNumber);
            throw new ResourceNotFoundException("Insufficient balance");
        }

        account.setBalance(account.getBalance()- amount);
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setType("DEPOSIT");
        txn.setAmount(amount);
        txn.setToAccount(account);
        txn.setStatus("SUCCESS");
        log.warn("Withdraw request: account={}, amount={}", accountNumber, amount);

        transactionRepository.save(txn);
        return new TransactionResponse(
                txn.getType(),
                txn.getAmount(),
                txn.getStatus(),
                null,
                account.getAccountNumber()
        );



    }

//transfer

    @Transactional
    public TransactionResponse transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        if (fromAccount.getBalance() < amount) {
            log.error("Insufficient balance for account {}", toAccountNumber);
            throw new ResourceNotFoundException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction txn = new Transaction();
        txn.setType("DEPOSIT");
        txn.setAmount(amount);
        txn.setToAccount(toAccount);
        txn.setStatus("SUCCESS");

        log.info("Transfer: from={}, to={}, amount={}", fromAccountNumber, toAccountNumber, amount);

        transactionRepository.save(txn);
        return new TransactionResponse(
                txn.getType(),
                txn.getAmount(),
                txn.getStatus(),
                null,
                toAccount.getAccountNumber()
        );
    }



}
