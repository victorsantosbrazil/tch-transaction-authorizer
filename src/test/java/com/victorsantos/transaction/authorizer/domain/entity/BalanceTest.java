package com.victorsantos.transaction.authorizer.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BalanceTest {

    @Test
    @DisplayName("Given a total amount when debit and balance has enough funds then debit and return zero")
    void givenTotalAmount_whenDebit_thenDebitAndReturnZero() {
        var totalAmount = BigDecimal.valueOf(100);
        var debitAmount = BigDecimal.valueOf(50);

        Balance balance = Balance.builder().totalAmount(totalAmount).build();

        BigDecimal remainingAmount = balance.debit(debitAmount);

        assertEquals(BigDecimal.ZERO, remainingAmount);

        var expectedBalance = totalAmount.subtract(debitAmount);
        assertEquals(expectedBalance, balance.getTotalAmount());
    }

    @Test
    @DisplayName("Given a total amount equals balance when debit then debit and return zero")
    void givenTotalAmountEqualsBalance_whenDebit_thenDebitAndReturnZero() {
        var totalAmount = BigDecimal.valueOf(100);
        var debitAmount = BigDecimal.valueOf(100);

        Balance balance = Balance.builder().totalAmount(totalAmount).build();

        BigDecimal remainingAmount = balance.debit(debitAmount);

        assertEquals(BigDecimal.ZERO, remainingAmount);
        assertEquals(BigDecimal.ZERO, balance.getTotalAmount());
    }

    @Test
    @DisplayName("Given a total amount exceeding balance when debit then debit and return remaining amount")
    void givenTotalAmountExceedingBalance_whenDebit_thenDebitAndReturnRemainingAmount() {
        var totalAmount = BigDecimal.valueOf(100);
        var debitAmount = BigDecimal.valueOf(150);

        Balance balance = Balance.builder().totalAmount(totalAmount).build();

        BigDecimal remainingAmount = balance.debit(debitAmount);

        assertEquals(debitAmount.subtract(totalAmount), remainingAmount);

        assertEquals(BigDecimal.ZERO, balance.getTotalAmount());
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
