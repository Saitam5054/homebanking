package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList());
    };

    @RequestMapping("transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return new TransactionDTO(transactionRepository.findById(id).orElse(null));
    }

    /*@RequestMapping("/clients/current/accounts/transaction")
    public ResponseEntity<Object> createTransaction(Authentication authentication) {

    }*/

    @Transactional
    @PostMapping("/transactions/")
    public ResponseEntity<Object> transfer(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String originAccount,
            @RequestParam String destinationAccount,
            @RequestParam TransactionType type,
            Authentication authentication) {

        if (amount == null || description.isBlank() || originAccount.isBlank() || destinationAccount.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Account origin = accountRepository.findByNumber(originAccount);
        Account destination = accountRepository.findByNumber(destinationAccount);

        if (origin == null || destination == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }

        if (origin.equals(destination)) {
            return new ResponseEntity<>("Same account", HttpStatus.BAD_REQUEST);
        }

        if (!origin.getClient().equals(authentication.getPrincipal())) {
            return new ResponseEntity<>("This is not your account", HttpStatus.FORBIDDEN);
        }

        if (origin.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
        }

        /*Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount, description + " " + originAccount);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, +amount, description + " " + destinationAccount);*/

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(amount);
        debitTransaction.setDescription(description + " " + destination);
        debitTransaction.setAccount(origin);
        debitTransaction.setType(type);
        debitTransaction.setDate(LocalDate.now());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount);
        creditTransaction.setDescription(description + " " + origin);
        creditTransaction.setAccount(destination);
        creditTransaction.setType(type);
        creditTransaction.setDate(LocalDate.now());

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        origin.setBalance(origin.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        accountRepository.save(origin);
        accountRepository.save(destination);

        return new ResponseEntity<>("Transaction completed", HttpStatus.CREATED);

    }
}
