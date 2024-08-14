package com.victorsantos.transaction.authorizer.application.service.balance;

import com.victorsantos.transaction.authorizer.domain.entity.Balance;
import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.Optional;

public interface BalanceService {

    Optional<Balance> findById(String accountId, BenefitCategory category);

    void save(Balance balance);
}
