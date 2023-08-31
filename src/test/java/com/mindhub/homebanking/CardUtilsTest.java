package com.mindhub.homebanking;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CardUtilsTest {

    @Test
    public void testGetCardNumber() {
        String cardNumber = CardUtils.getCardNumber();
        Assertions.assertNotNull(cardNumber);
        Assertions.assertEquals(16, cardNumber.length());
    }

    @Test
    public void testGetCVV() {
        int cvv = CardUtils.getCVV();
        Assertions.assertTrue(cvv >= 100 && cvv <= 999);
    }
}