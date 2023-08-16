package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    private ClientLoanRepository clientloanRepository;

    @RequestMapping("/clientLoans")
    public List<ClientLoanDTO> getClientLoan() {
        /*List<ClientLoan> listClientLoan = clientloanRepository.findAll();
        List<ClientLoanDTO> listClientLoanDTO = listClientLoan.stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
        return listClientLoanDTO;*/

        return clientloanRepository.findAll().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clientLoans/{id}")
    public ClientLoanDTO getClientLoan(@PathVariable Long id) {
        return new ClientLoanDTO((clientloanRepository.findById(id).orElse(null)));
    }
}
