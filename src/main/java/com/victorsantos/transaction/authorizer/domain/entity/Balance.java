package com.victorsantos.transaction.authorizer.domain.entity;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Balance {
    private String accountId;
    private BenefitCategory category;
    private BigDecimal totalAmount;

    private boolean hasEnoughBalance(BigDecimal amount) {
        return this.totalAmount.compareTo(amount) >= 0;
    }

    public boolean debit(BigDecimal amount) {
        if (!hasEnoughBalance(amount)) return false;

        this.totalAmount = this.totalAmount.subtract(amount);
        return true;
    }
}
