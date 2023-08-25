package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        /*List<Account> listAccount = accountRepository.findAll();

        List<AccountDTO> listAccountDTO = listAccount.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());

        return listAccountDTO;*/

        // return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());

        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    };

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return new AccountDTO((accountRepository.findById(id).orElse(null)));
    }

    /*@RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount (@PathVariable Long id) {
        Client account = clientRepository.findById(id).orElse(null);
        if (account != null) {
            clientRepository.save(account);
            return new ResponseEntity<>(new ClientDTO(account), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Not found", HttpStatus.FORBIDDEN);
        }
            @RequestParam Client client, @RequestParam String number, @RequestParam Double balance) {

        if (client.isEmpty() || number.isEmpty() || balance.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

    }*/

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@AuthenticationPrincipal UserDetails userDetails) {
        Client client = clientRepository.findByEmail(userDetails.getUsername());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Maximum account limit", HttpStatus.FORBIDDEN);
        }

        Account account = new Account();

        account.setNumber(generateAccountNumber());
        account.setBalance(0D);
        account.setClient(client);
        accountRepository.save(account);

        return new ResponseEntity<>("Account created", HttpStatus.CREATED);

    }

    private String generateAccountNumber() {
        Random random = new Random();
        int accountNumber = random.nextInt(99999999) +1;
        return "VIN-" + String.format("%07d", accountNumber);
    }
}
