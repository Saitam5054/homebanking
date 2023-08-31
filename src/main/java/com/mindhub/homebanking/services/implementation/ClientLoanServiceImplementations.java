package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientLoanServiceImplementations implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public List<ClientLoanDTO> getClientLoansDTO() {
        return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public ClientLoanDTO getClientLoanDTOById(Long id) {
        return new ClientLoanDTO((clientLoanRepository.findById(id).orElse(null)));
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
