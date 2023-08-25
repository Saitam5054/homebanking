package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

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

    /*@RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> register (
            @RequestParam Client client, @RequestParam String number, @RequestParam Double balance) {

        if (client.isEmpty() || number.isEmpty() || balance.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
    }*/
}
