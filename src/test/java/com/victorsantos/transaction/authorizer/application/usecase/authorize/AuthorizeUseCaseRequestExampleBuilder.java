package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import java.math.BigDecimal;

public class AuthorizeUseCaseRequestExampleBuilder {

    public static AuthorizeUseCaseRequest oneAuthorizeUseCaseRequest() {
        return builder().build();
    }

    public static AuthorizeUseCaseRequest.AuthorizeUseCaseRequestBuilder builder() {
        return AuthorizeUseCaseRequest.builder()
                .account("123")
                .totalAmount(BigDecimal.valueOf(100))
                .mcc("5811")
                .merchant("PADARIA DO ZE SAO PAULO BR");
    }
}
