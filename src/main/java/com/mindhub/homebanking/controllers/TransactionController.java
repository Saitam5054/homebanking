package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionService.getTransactionsDTO();
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            return new ResponseEntity<>(new TransactionDTO(transaction), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transfer(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            Authentication authentication) {

        if (amount == null || description.isBlank() || fromAccountNumber.isBlank() || toAccountNumber.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.NOT_FOUND);
        }

        Account origin = accountRepository.findByNumber(fromAccountNumber);
        Account destination = accountRepository.findByNumber(toAccountNumber);

        if (origin == null || destination == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (!origin.getClient().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("This is not your account", HttpStatus.FORBIDDEN);
        }

        if (origin.equals(destination)) {
            return new ResponseEntity<>("Cannot transfer to the same account", HttpStatus.BAD_REQUEST);
        }

        if (origin.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
        }

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(-amount);
        debitTransaction.setDescription(description + " (To: " + destination.getNumber() + ")");
        debitTransaction.setAccount(origin);
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction.setDate(LocalDate.now());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount);
        creditTransaction.setDescription(description + " (From: " + origin.getNumber() + ")");
        creditTransaction.setAccount(destination);
        creditTransaction.setType(TransactionType.CREDIT);
        creditTransaction.setDate(LocalDate.now());

        //transactionRepository.save(debitTransaction);
        //transactionRepository.save(creditTransaction);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        origin.setBalance(origin.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        //accountRepository.save(origin);
        //accountRepository.save(destination);

        accountService.saveAccount(origin);
        accountService.saveAccount(destination);

        return new ResponseEntity<>("Transaction completed", HttpStatus.CREATED);
    }
}