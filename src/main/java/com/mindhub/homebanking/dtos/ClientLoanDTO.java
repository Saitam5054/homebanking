package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private Long id;
    private String name;
    private Double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientloan) {
        id = clientloan.getId();
        name = clientloan.getLoan().getName();
        amount = clientloan.getAmount();
        payments = clientloan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}
