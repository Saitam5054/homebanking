package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private Double maxAmount;

    @ElementCollection
    private List<Integer> payments;
    // private List<Integer> payments = new ArrayList<>();

    /*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client clients;*/

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "loan")
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "loan")
    private Set<ClientLoan> clients = new HashSet<>();

    public Loan() {

    }

    public Loan(String name, Double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClients() {
        return clients;
    }

    public void setClients(Set<ClientLoan> clients) {
        this.clients = clients;
    }
    
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
    
/*    public void setClients(Set<ClientLoan> clients) {
        this.clients = clients;
    }*/

/*    public void addClientLoan (ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clients.add(clientLoan);
    }*/

    /*public List<Client> getClients(){
        return clients.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toList());
    }*/
}
