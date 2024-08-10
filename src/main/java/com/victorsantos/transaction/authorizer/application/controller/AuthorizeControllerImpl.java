package com.victorsantos.transaction.authorizer.application.controller;

import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCase;
import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseRequest;
import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseResponse;
import com.victorsantos.transaction.authorizer.domain.constant.AuthorizationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class AuthorizeControllerImpl implements AuthorizeController {

    private final AuthorizeUseCase authorizeUseCase;

    @Override
    public AuthorizeUseCaseResponse authorize(AuthorizeUseCaseRequest request) {
        try {
            return authorizeUseCase.run(request);
        } catch (Exception e) {
            log.error("Error while authorizing transaction", e);
            return new AuthorizeUseCaseResponse(AuthorizationCode.OTHER);
        }
    }
}
