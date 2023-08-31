package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import java.util.List;

public class LoanApplicationDTO {

    private Long loanId;
    private Double amount;
    private Integer payments;
    private String toAccountNumber;

    public LoanApplicationDTO() {
    }

    /*public LoanApplicationDTO(Loan loan) {
        loanId = loan.getId();
        maxAmount = loan.getMaxAmount();
        payments = loan.getPayments();
        toAccountNumber = loan.getName();
    }*/

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
}