package com.victorsantos.transaction.authorizer.infra.data.model;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.math.BigDecimal;

public class BalanceModelExampleBuilder {

    private BalanceModelExampleBuilder() {}

    public static BalanceModel oneBalanceModel() {
        return builder().build();
    }

    public static BalanceModel.BalanceModelBuilder builder() {
        return BalanceModel.builder()
                .accountId("123")
                .category(BenefitCategory.FOOD)
                .totalAmount(BigDecimal.valueOf(1000));
    }
}
