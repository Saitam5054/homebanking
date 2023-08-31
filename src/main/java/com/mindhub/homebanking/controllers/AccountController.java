package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private AccountService accountService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        /*List<Account> listAccount = accountRepository.findAll();
        List<AccountDTO> listAccountDTO = listAccount.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return listAccountDTO;*/
        // return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());

        // return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());

        return accountService.getAccountsDTO();
    };

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        //return new AccountDTO((accountRepository.findById(id).orElse(null)));

        return accountService.getAccountDTOById(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

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

        // accountRepository.save(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<Object> getClientAccounts(Authentication authentication) {
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);

        List<AccountDTO> accountDTOs = client.getAccounts().stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDTOs);
    }
    private String generateAccountNumber() {
        Random random = new Random();
        int accountNumber = random.nextInt(99999999) +1;
        return "VIN-" + String.format("%07d", accountNumber);
    }
}
