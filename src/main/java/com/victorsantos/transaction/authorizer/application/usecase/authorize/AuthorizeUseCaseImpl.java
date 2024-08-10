package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import com.victorsantos.transaction.authorizer.application.service.balance.BalanceService;
import com.victorsantos.transaction.authorizer.application.service.benefit.BenefitCategoryService;
import com.victorsantos.transaction.authorizer.domain.constant.AuthorizationCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
class AuthorizeUseCaseImpl implements AuthorizeUseCase {

    private final Validator validator;

    private final BalanceService balanceService;

    private final BenefitCategoryService benefitCategoryService;

    @Override
    public AuthorizeUseCaseResponse run(AuthorizeUseCaseRequest request) {
        var transactionId = UUID.randomUUID().toString();

        log.info("Authorizing transaction... Id: {}, Request: {}", transactionId, request);

        var validationResult = validator.validateObject(request);
        if (validationResult.hasErrors()) {
            log.error(
                    "Transaction failed. Cause: Found one or more invalid fields in request. Id: {}, Errors: {}",
                    transactionId,
                    validationResult.getAllErrors());
            return new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        }

        var category = benefitCategoryService.findByMcc(request.getMcc());

        var optionalBalance = balanceService.findById(request.getAccount(), category);

        if (optionalBalance.isEmpty()) {
            log.error(
                    "Transaction failed. Cause: Balance not found for account {} and category {}. Id: {}",
                    request.getAccount(),
                    category,
                    transactionId);
            return new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        }

        var balance = optionalBalance.get();
        var wasDebited = balance.debit(request.getTotalAmount());
        if (!wasDebited) {
            log.error(
                    "Transaction failed. Cause: Insufficient balance for account {} and category {}. Id: {}",
                    request.getAccount(),
                    category,
                    transactionId);
            return new AuthorizeUseCaseResponse(AuthorizationCode.REFUSED);
        }

        balanceService.save(balance);

        log.info("Transaction authorized. Id: {}", transactionId);
        return new AuthorizeUseCaseResponse(AuthorizationCode.APPROVED);
    }
}
