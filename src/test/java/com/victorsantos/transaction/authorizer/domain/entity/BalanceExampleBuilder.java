package com.victorsantos.transaction.authorizer.domain.entity;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.math.BigDecimal;

public class BalanceExampleBuilder {

    private BalanceExampleBuilder() {}

    public static Balance oneBalance() {
        return builder().build();
    }

    public static Balance.BalanceBuilder builder() {
        return Balance.builder().accountId("123").category(BenefitCategory.FOOD).totalAmount(BigDecimal.valueOf(1000));
    }
}
