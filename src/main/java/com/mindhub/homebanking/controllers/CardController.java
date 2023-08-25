package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/clients/current/cards")
public class CardController {

    private ClientRepository clientRepository;
    private CardRepository cardRepository;

    @PostMapping
    public ResponseEntity<Object> createCards (
            @RequestParam CardColor color, @RequestParam CardType type, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        List<Card> cards = cardRepository.findByCardHolder(client);

        if (cards.size() >= 3) {
            return new ResponseEntity<>("You already have the maximum cards", HttpStatus.FORBIDDEN);
        }

        Card card = new Card();

        card.setColor(color);
        card.setType(type);
        card.setClient(client);

        card.setCvv(new Random().nextInt(1000) + 1);
        card.setNumber(generateCardNumber());
        card.setCardHolder(client.getFirstName() + " " + client.getLastName());
        card.setFromDate(LocalDate.now());
        card.setThruDate(LocalDate.now().plusYears(5));

        cardRepository.save(card);

        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                int digit = random.nextInt(10);
                cardNumber.append(digit);
            }
            if (i<3) {
                cardNumber.append("-");
            }
        }
        return cardNumber.toString();
    }
}
