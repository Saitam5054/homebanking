package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients/current/cards")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<Object> createCard(
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        long typeCount = cardRepository.countByClientAndType(client, cardType);

        if (typeCount >= 3) {
            return new ResponseEntity<>("You've reached the limit of cards of this type", HttpStatus.FORBIDDEN);
        }

        String cardNumber = CardUtils.getCardNumber();
        int cvv = CardUtils.getCVV();

        Card card = new Card();
        card.setColor(cardColor);
        card.setType(cardType);
        card.setClient(client);
        card.setCvv(cvv);
        card.setNumber(cardNumber);
        card.setCardHolder(client.getFirstName() + " " + client.getLastName());
        card.setFromDate(LocalDate.now());
        card.setThruDate(LocalDate.now().plusYears(5));

        // cardRepository.save(card);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        List<CardDTO> cardDTOs = client.getCards().stream()
                .map(CardDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardDTOs);
    }
}