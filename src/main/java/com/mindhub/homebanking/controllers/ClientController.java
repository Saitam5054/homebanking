package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {

        /*List<Client> listClient = clientRepository.findAll();

        List<ClientDTO> listClientDTO = listClient.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());

        return listClientDTO;*/

        // return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());

        // return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());

        return clientService.getClientsDTO();
    };

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {

        /*Optional<Client> optionalClient = clientRepository.findById(id);
        Client client = optionalClient.orElse(null);
        ClientDTO clientDTO = new ClientDTO(client);*/

        //return new ClientDTO(clientRepository.findById(id).orElse(null));

        return clientService.getClientDTOById(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register (
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if(firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName,lastName, email, passwordEncoder.encode(password));
        //clientRepository.save(client);
        clientService.saveClient(client);

        // clientRepository.save(new Client(firstName,lastName,email,passwordEncoder.encode(password)));
        // return new ResponseEntity<>("Register", HttpStatus.CREATED);

        // clientRepository.save(client);

        Account account = new Account("First Account", LocalDate.now(), 0D);
        account.setClient(client);
        //accountRepository.save(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Register", HttpStatus.CREATED);

    }
    @GetMapping("clients/current")
    public ClientDTO getCurrent(Authentication authentication) {
        //return new ClientDTO(clientRepository.findByEmail(authentication.getName()));

        return clientService.getClientDTOByEmail(authentication);
    }

    /*@RequestMapping("/api/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getEmail().orElse(null));
    }*/
}
