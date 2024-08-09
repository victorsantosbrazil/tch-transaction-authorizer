package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthorizeUseCaseResponse {
    private String code;
}
