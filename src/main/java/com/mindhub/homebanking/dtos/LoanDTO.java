package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Set;

public class LoanDTO {

    private Long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments;
    private Client clients;
    private Set<ClientLoan> clientLoans;

    // private Set<Loan> loans;

    public LoanDTO(Loan loan) {
        id = loan.getId();
        name = loan.getName();
        maxAmount = loan.getMaxAmount();
        payments = loan.getPayments();
        clients = (Client) loan.getClients();
        clientLoans = loan.getClientLoans();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Client getClients() {
        return clients;
    }

    /*public Set<Loan> getLoans() {
        return loans;
    }*/

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
}
