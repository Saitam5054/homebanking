package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
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
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private ClientLoanService clientLoanService;

    @RequestMapping("/clientLoans")
    public List<ClientLoanDTO> getClientLoan() {
        /*List<ClientLoan> listClientLoan = clientLoanRepository.findAll();
        List<ClientLoanDTO> listClientLoanDTO = listClientLoan.stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
        return listClientLoanDTO;*/

        // return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(Collectors.toList());

        return clientLoanService.getClientLoansDTO();
    }

    @RequestMapping("/clientLoans/{id}")
    public ClientLoanDTO getClientLoan(@PathVariable Long id) {
        //return new ClientLoanDTO((clientLoanRepository.findById(id).orElse(null)));
        return clientLoanService.getClientLoanDTOById(id);
    }
}