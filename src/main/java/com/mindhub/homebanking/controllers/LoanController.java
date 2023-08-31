package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        //return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
        return loanService.getLoansDTO();
    }

    @RequestMapping("/loans/{id}")
    public LoanDTO getLoan(@PathVariable Long id) {
        return new LoanDTO((loanRepository.findById(id).orElse(null)));
    }

    @Transactional
    // @PostMapping("/clients/current/loans")
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication
    ) {

        Client client = clientRepository.findByEmail(authentication.getName());

        Double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }

        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.BAD_REQUEST);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Loan amount exceeds maximum allowed", HttpStatus.BAD_REQUEST);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Invalid number of payments", HttpStatus.BAD_REQUEST);
        }

        Account toAccountNumber = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        if (toAccountNumber == null) {
            return new ResponseEntity<>("Destination account doesn't exists", HttpStatus.NOT_FOUND);
        }

        if (!toAccountNumber.getClient().equals(client)) {
            return new ResponseEntity<>("This account doesn't belong to the client", HttpStatus.BAD_REQUEST);
        }

        Double totalAmount = loanApplicationDTO.getAmount() * 1.2;

        Transaction transaction = new Transaction(TransactionType.CREDIT, totalAmount, loan.getName() + " loan approved", LocalDate.now());

        //transactionRepository.save(transaction);
        transactionService.saveTransaction(transaction);

        toAccountNumber.setBalance(toAccountNumber.getBalance() + loanApplicationDTO.getAmount());

        //accountRepository.save(toAccountNumber);
        accountService.saveAccount(toAccountNumber);

        ClientLoan clientLoan = new ClientLoan(amount, payments, client, loan);

        //clientLoanRepository.save(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Loan successful", HttpStatus.CREATED);
    }
}