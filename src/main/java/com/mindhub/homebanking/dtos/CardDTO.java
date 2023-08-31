package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDate;

public class CardDTO {

    private Long id;
    private String cardHolder, number;
    private int cvv;
    private CardType type;
    private CardColor color;
    private LocalDate fromDate, thruDate;

    public CardDTO() { }

    public CardDTO(Card card) {
        id = card.getId();
        cardHolder = card.getCardHolder();
        number = card.getNumber();
        cvv = card.getCvv();
        type = card.getType();
        color = card.getColor();
        fromDate = card.getFromDate();
        thruDate = card.getThruDate();
    }

    public Long getId() {
        return id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public String getNumber() {
        return number;
    }
    public int getCvv() {
        return cvv;
    }
    public CardType getType() {
        return type;
    }
    public CardColor getColor() {
        return color;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public LocalDate getThruDate() {
        return thruDate;
    }
}