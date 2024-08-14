package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.victorsantos.transaction.authorizer.application.constant.AuthorizationCode;
import com.victorsantos.transaction.authorizer.application.service.balance.BalanceService;
import com.victorsantos.transaction.authorizer.application.service.benefit.BenefitCategoryService;
import com.victorsantos.transaction.authorizer.domain.entity.Balance;
import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.FieldError;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.Validator;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuthorizeUseCaseImpl.class})
class AuthorizeUseCaseTest {

    @Autowired
    private AuthorizeUseCase usecase;

    @MockBean
    private Validator validator;

    @MockBean
    private BalanceService balanceService;

    @MockBean
    private BenefitCategoryService benefitCategoryService;

    @Test
    @DisplayName("Given request with amount lower than balance, then return response with 'approved' code")
    void givenRequestWithAmountLowerThanBalance_thenReturnResponseWithApprovedCode() {
        var accountId = "123";

        var category = BenefitCategory.CASH;

        var request = AuthorizeUseCaseRequest.builder()
                .account("123")
                .totalAmount(BigDecimal.valueOf(100))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var balance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var noValidationError = new SimpleErrors(request);

        when(validator.validateObject(request)).thenReturn(noValidationError);
        when(benefitCategoryService.findByMerchantNameAndMcc(request.getMerchant(), request.getMcc()))
                .thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(balance));

        var response = usecase.run(request);

        var balanceSavedCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(balanceService, times(1)).save(balanceSavedCaptor.capture());

        var balanceSaved = balanceSavedCaptor.getValue();
        assertEquals(BigDecimal.valueOf(900), balanceSaved.getTotalAmount());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.APPROVED);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @ParameterizedTest
    @EnumSource(
            value = BenefitCategory.class,
            names = {"FOOD", "MEAL"})
    @DisplayName(
            "Given request with amount exceeding not cash balance and cash balance can process remaining, then return response with 'approved' code")
    void
            givenRequestWithAmountExceedingNotCashBalanceAndCashBalanceCanProcessRemaining_thenReturnResponseWithApprovedCode(
                    BenefitCategory category) {
        var accountId = "123";

        var request = AuthorizeUseCaseRequest.builder()
                .account("123")
                .totalAmount(BigDecimal.valueOf(1500))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var foodBalance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var cashBalance = Balance.builder()
                .accountId(accountId)
                .category(BenefitCategory.CASH)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var noValidationError = new SimpleErrors(request);

        when(validator.validateObject(request)).thenReturn(noValidationError);
        when(benefitCategoryService.findByMerchantNameAndMcc(request.getMerchant(), request.getMcc()))
                .thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(foodBalance));
        when(balanceService.findById(accountId, BenefitCategory.CASH)).thenReturn(Optional.of(cashBalance));

        var response = usecase.run(request);

        var balanceSavedCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(balanceService, times(2)).save(balanceSavedCaptor.capture());

        var balancesSaved = balanceSavedCaptor.getAllValues();
        var cashBalanceSaved = balancesSaved.get(0);
        var foodBalanceSaved = balancesSaved.get(1);

        assertEquals(BigDecimal.valueOf(500), cashBalanceSaved.getTotalAmount());
        assertEquals(BigDecimal.ZERO, foodBalanceSaved.getTotalAmount());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.APPROVED);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @ParameterizedTest
    @EnumSource(
            value = BenefitCategory.class,
            names = {"FOOD", "MEAL"})
    @DisplayName(
            "Given request to a not cash balance and value exceeding both its balance and cash balance together, then return response with 'refused' code")
    void
            givenRequestToNotCashBalanceAndAmountExceedingItsBalanceAndCashBalanceTogether_thenReturnResponseWithRefusedCode(
                    BenefitCategory category) {
        var accountId = "123";

        var request = AuthorizeUseCaseRequest.builder()
                .account("123")
                .totalAmount(BigDecimal.valueOf(2001))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var foodBalance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var cashBalance = Balance.builder()
                .accountId(accountId)
                .category(BenefitCategory.CASH)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var noValidationError = new SimpleErrors(request);

        when(validator.validateObject(request)).thenReturn(noValidationError);
        when(benefitCategoryService.findByMerchantNameAndMcc(request.getMerchant(), request.getMcc()))
                .thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(foodBalance));
        when(balanceService.findById(accountId, BenefitCategory.CASH)).thenReturn(Optional.of(cashBalance));

        var response = usecase.run(request);

        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.REFUSED);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @Test
    @DisplayName(
            "Given request to cash balance and amount exceeding its balance, then return response with 'refused' code")
    void givenRequestToCashBalanceAndAmountExceedingBalance_thenReturnResponseWithRefusedCode() {
        var accountId = "123";

        var category = BenefitCategory.CASH;

        var request = AuthorizeUseCaseRequest.builder()
                .account("123")
                .totalAmount(BigDecimal.valueOf(1000.01))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var balance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var noValidationError = new SimpleErrors(request);

        when(validator.validateObject(request)).thenReturn(noValidationError);
        when(benefitCategoryService.findByMerchantNameAndMcc(request.getMerchant(), request.getMcc()))
                .thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(balance));

        var response = usecase.run(request);

        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.REFUSED);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @Test
    @DisplayName("Given request with invalid fields, then return response with 'other' code")
    void givenRequestWithInvalidFields_thenReturnResponseWithOtherCode() {
        var request = AuthorizeUseCaseRequest.builder()
                .account("")
                .totalAmount(null)
                .mcc("")
                .merchant("")
                .build();

        var validationErrors = new SimpleErrors(request);
        validationErrors
                .getFieldErrors()
                .add(new FieldError("AuthorizeUseCaseRequest", "account", "must not be blank"));

        when(validator.validateObject(request)).thenReturn(validationErrors);

        var response = usecase.run(request);

        verify(benefitCategoryService, never()).findByMerchantNameAndMcc(any(), any());
        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @Test
    @DisplayName("Given request to a non-existent balance, then return response with 'other' code")
    void givenRequestToNonExistentBalance_thenReturnResponseWithOtherCode() {
        var accountId = "123";

        var category = BenefitCategory.CASH;

        var request = AuthorizeUseCaseRequest.builder()
                .account("123")
                .totalAmount(BigDecimal.valueOf(100))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var noValidationError = new SimpleErrors(request);

        when(validator.validateObject(request)).thenReturn(noValidationError);
        when(benefitCategoryService.findByMerchantNameAndMcc(request.getMerchant(), request.getMcc()))
                .thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.empty());

        var response = usecase.run(request);

        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }

    @ParameterizedTest
    @EnumSource(
            value = BenefitCategory.class,
            names = {"FOOD", "MEAL"})
    @DisplayName(
            "Given request to a not cash balance and amount exceeding its balance and cash balance not found, then return response with 'other' code")
    void
            givenRequestToNotCashBalanceAndAmountExceedsItsBalanceAndCashBalanceNotFound_thenReturnResponseWithApprovedCode(
                    BenefitCategory category) {
        var accountId = "123";

        var request = AuthorizeUseCaseRequest.builder()
                .account(accountId)
                .totalAmount(BigDecimal.valueOf(1500))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR")
                .build();

        var foodBalance = Balance.builder()
                .accountId(accountId)
                .category(category)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        var noValidationError = new SimpleErrors(request);

        when(validator.validateObject(request)).thenReturn(noValidationError);
        when(benefitCategoryService.findByMerchantNameAndMcc(request.getMerchant(), request.getMcc()))
                .thenReturn(category);
        when(balanceService.findById(accountId, category)).thenReturn(Optional.of(foodBalance));
        when(balanceService.findById(accountId, BenefitCategory.CASH)).thenReturn(Optional.empty());

        var response = usecase.run(request);

        verify(balanceService, never()).save(any());

        var expectedResponse = new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        assertEquals(expectedResponse.getCode(), response.getCode());
    }
}
