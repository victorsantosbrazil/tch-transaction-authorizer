package com.victorsantos.transaction.authorizer.infra.data.model;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BalanceModelId {
    private String accountId;
    private BenefitCategory category;
}
