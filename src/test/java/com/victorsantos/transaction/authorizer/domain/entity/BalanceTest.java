package com.victorsantos.transaction.authorizer.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BalanceTest {

    @Test
    @DisplayName("Given a total amount when debit and balance has enough funds then debit and return true")
    void givenTotalAmount_whenDebit_thenDebitAndReturnTrue() {
        var totalAmount = BigDecimal.valueOf(100);
        var debitAmount = BigDecimal.valueOf(50);

        Balance balance = Balance.builder().totalAmount(totalAmount).build();

        boolean result = balance.debit(debitAmount);

        assertTrue(result);

        var expectedBalance = totalAmount.subtract(debitAmount);
        assertEquals(expectedBalance, balance.getTotalAmount());
    }

    @Test
    @DisplayName("Given a total amount exceeding balance when debit then do not debit and return false")
    void givenTotalAmountExceedingBalance_whenDebit_thenDebitAndReturnTrue() {
        var totalAmount = BigDecimal.valueOf(100);
        var debitAmount = BigDecimal.valueOf(150);

        Balance balance = Balance.builder().totalAmount(totalAmount).build();

        boolean result = balance.debit(debitAmount);

        assertFalse(result);
        assertEquals(totalAmount, balance.getTotalAmount());
    }

    @Test
    @DisplayName("Given a negative amount when debit then throw illegal argument exception")
    void givenNegativeTotalAmount_whenDebit_thenThrowIllegalArgumentException() {
        var totalAmount = BigDecimal.valueOf(100);
        var debitAmount = BigDecimal.valueOf(-100);

        Balance balance = Balance.builder().totalAmount(totalAmount).build();

        assertThrows(IllegalArgumentException.class, () -> balance.debit(debitAmount));
    }
}
