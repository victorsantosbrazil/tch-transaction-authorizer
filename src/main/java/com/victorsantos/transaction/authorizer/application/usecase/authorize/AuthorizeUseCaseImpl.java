package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import com.victorsantos.transaction.authorizer.application.service.BalanceService;
import com.victorsantos.transaction.authorizer.application.service.BenefitCategoryService;
import com.victorsantos.transaction.authorizer.domain.constant.AuthorizationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AuthorizeUseCaseImpl implements AuthorizeUseCase {

    private final BalanceService balanceService;

    private final BenefitCategoryService benefitCategoryService;

    @Override
    public AuthorizeUseCaseResponse run(AuthorizeUseCaseRequest request) {
        var category = benefitCategoryService.findByMcc(request.getMcc());

        var optionalBalance = balanceService.findById(request.getAccountId(), category);

        if (optionalBalance.isEmpty()) {
            return new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        }

        var balance = optionalBalance.get();
        var wasDebited = balance.debit(request.getTotalAmount());
        if (!wasDebited) {
            return new AuthorizeUseCaseResponse(AuthorizationCode.REFUSED);
        }

        balanceService.save(balance);
        return new AuthorizeUseCaseResponse(AuthorizationCode.APPROVED);
    }
}
