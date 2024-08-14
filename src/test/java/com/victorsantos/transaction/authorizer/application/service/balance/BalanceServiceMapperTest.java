package com.victorsantos.transaction.authorizer.application.service.balance;

import static com.victorsantos.transaction.authorizer.domain.entity.BalanceExampleBuilder.oneBalance;
import static com.victorsantos.transaction.authorizer.infra.data.model.BalanceModelExampleBuilder.oneBalanceModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BalanceServiceMapperImpl.class})
class BalanceServiceMapperTest {

    @Autowired
    private BalanceServiceMapper balanceServiceMapper;

    @Test
    @DisplayName("Given a model when toEntity then return mapped entity")
    void givenModel_whenToEntity_thenReturnMappedEntity() {
        var model = oneBalanceModel();

        var entity = balanceServiceMapper.toEntity(model);

        assertEquals(model.getAccountId(), entity.getAccountId());
        assertEquals(model.getCategory(), entity.getCategory());
        assertEquals(model.getTotalAmount(), entity.getTotalAmount());
    }

    @Test
    @DisplayName("Given an entity when toModel then return mapped model")
    void givenEntity_whenToModel_thenReturnMappedModel() {
        var entity = oneBalance();

        var model = balanceServiceMapper.toModel(entity);

        assertEquals(entity.getAccountId(), model.getAccountId());
        assertEquals(entity.getCategory(), model.getCategory());
        assertEquals(entity.getTotalAmount(), model.getTotalAmount());
    }
}
