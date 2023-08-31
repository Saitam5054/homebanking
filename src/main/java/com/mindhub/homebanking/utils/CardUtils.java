package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    private CardUtils() {
    }

    public static String getCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int digit = random.nextInt(10);
                cardNumber.append(digit);
            }
            if (i < 3) {
                cardNumber.append("-");
            }
        }
        return cardNumber.toString();
    }

    public static int getCVV() {
        return new Random().nextInt(1000) + 1;
    }

}