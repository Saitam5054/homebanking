package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoan() {
        /*List<Loan> listLoan = loanRepository.findAll();
        List<LoanDTO> listLoanDTO = listLoan.stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
        return listLoanDTO;*/

        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/loans/{id}")
    public LoanDTO getLoan(@PathVariable Long id) {
        return new LoanDTO((loanRepository.findById(id).orElse(null)));
    }
}
