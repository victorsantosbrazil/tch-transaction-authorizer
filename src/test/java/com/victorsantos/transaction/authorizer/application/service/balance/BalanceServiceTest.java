package com.victorsantos.transaction.authorizer.application.service.balance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.victorsantos.transaction.authorizer.domain.entity.Balance;
import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import com.victorsantos.transaction.authorizer.infra.data.BalanceRepository;
import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModel;
import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModelId;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BalanceServiceImpl.class})
class BalanceServiceTest {

    @Autowired
    private BalanceService balanceService;

    @MockBean
    private BalanceServiceMapper balanceServiceMapper;

    @MockBean
    private BalanceRepository balanceRepository;

    @Test
    @DisplayName("Given existent id, when find balance by id, then return optional with balance")
    void givenExistentId_whenFindBalanceById_thenReturnOptionalWithBalance() {
        var accountId = "1";
        var category = BenefitCategory.CASH;
        var totalAmount = BigDecimal.valueOf(1000);

        var balanceModel = BalanceModel.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(totalAmount)
                .build();

        var balanceModelId = new BalanceModelId(accountId, category);
        when(balanceRepository.findById(balanceModelId)).thenReturn(Optional.of(balanceModel));

        var expectedBalance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(totalAmount)
                .build();

        when(balanceServiceMapper.toEntity(balanceModel)).thenReturn(expectedBalance);

        var response = balanceService.findById(accountId, category);

        var expectedResponse = Optional.of(expectedBalance);
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Given not existent id, when find balance by id, then return optional empty")
    void givenNotExistentId_whenFindBalanceById_thenReturnOptionalEmpty() {
        var accountId = "1";
        var category = BenefitCategory.CASH;

        var balanceModelId = new BalanceModelId(accountId, category);
        when(balanceRepository.findById(balanceModelId)).thenReturn(Optional.empty());

        var response = balanceService.findById(accountId, category);

        assertEquals(Optional.empty(), response);
    }

    @Test
    @DisplayName("Given entity, when save, then save entity")
    void givenEntity_whenSave_thenSaveEntity() {
        var balance = Balance.builder()
                .accountId("1")
                .category(BenefitCategory.CASH)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var balanceModel = BalanceModel.builder()
                .accountId(balance.getAccountId())
                .category(balance.getCategory())
                .totalAmount(balance.getTotalAmount())
                .build();

        when(balanceServiceMapper.toModel(balance)).thenReturn(balanceModel);

        balanceService.save(balance);

        verify(balanceRepository, times(1)).save(balanceModel);
    }
}
