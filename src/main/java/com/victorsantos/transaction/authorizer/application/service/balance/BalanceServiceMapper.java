package com.victorsantos.transaction.authorizer.application.service.balance;

import com.victorsantos.transaction.authorizer.domain.entity.Balance;
import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface BalanceServiceMapper {
    Balance toEntity(BalanceModel model);

    BalanceModel toModel(Balance balance);
}
