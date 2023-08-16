package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private Long id;
    private String name;
    private Double amount;
    private int payments;
    private Long loanId;

    public ClientLoanDTO(ClientLoan clientLoan) {
        id = clientLoan.getId();
        name = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
        loanId = clientLoan.getLoan().getId();
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

    public Long getLoanId() {
        return loanId;
    }
}
