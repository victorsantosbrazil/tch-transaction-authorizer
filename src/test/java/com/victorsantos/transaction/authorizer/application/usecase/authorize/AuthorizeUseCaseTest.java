package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.victorsantos.transaction.authorizer.application.service.BalanceService;
import com.victorsantos.transaction.authorizer.application.service.BenefitCategoryService;
import com.victorsantos.transaction.authorizer.domain.constant.AuthorizationCode;
import com.victorsantos.transaction.authorizer.domain.entity.Balance;
import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuthorizeUseCaseImpl.class})
class AuthorizeUseCaseTest {

    @Autowired
    private AuthorizeUseCase usecase;

    @MockBean
    private BalanceService balanceService;

    @MockBean
    private BenefitCategoryService benefitCategoryService;

    @Test
    @DisplayName("Given a valid request, then authorize transaction and return response with 'approved' code")
    void givenValidRequest_thenAuthorizeAndReturnResponseWithApprovedCode() {
        var accountId = "123";

        var category = BenefitCategory.CASH;

        var request = AuthorizeUseCaseRequest.builder()
                .accountId("123")
                .totalAmount(BigDecimal.valueOf(100))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var balance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        when(benefitCategoryService.findByMcc(request.getMcc())).thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(balance));

        var response = usecase.run(request);

        var balanceSavedCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(balanceService, times(1)).save(balanceSavedCaptor.capture());

        var balanceSaved = balanceSavedCaptor.getValue();
        assertEquals(BigDecimal.valueOf(900), balanceSaved.getTotalAmount());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.APPROVED);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @Test
    @DisplayName(
            "Given request with total amount exceeding balance, then do not authorize transaction and return response with 'refused' code")
    void givenRequestWithTotalAmountExceedingBalance_thenDoNotAuthorizeAndReturnResponseWithRefusedCode() {
        var accountId = "123";

        var category = BenefitCategory.CASH;

        var request = AuthorizeUseCaseRequest.builder()
                .accountId("123")
                .totalAmount(BigDecimal.valueOf(1000.01))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var balance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        when(benefitCategoryService.findByMcc(request.getMcc())).thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(balance));

        var response = usecase.run(request);

        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.REFUSED);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @Test
    @DisplayName(
            "Given request for not existent balance, then do not authorize transaction and return response with 'other' code")
    void givenRequestForNotExistentBalance_thenDoNotAuthorizeAndReturnResponseWithOtherCode() {
        var accountId = "123";

        var category = BenefitCategory.CASH;

        var request = AuthorizeUseCaseRequest.builder()
                .accountId("123")
                .totalAmount(BigDecimal.valueOf(100))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        when(benefitCategoryService.findByMcc(request.getMcc())).thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.empty());

        var response = usecase.run(request);

        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }
}