package com.victorsantos.transaction.authorizer.application.usecase.authorize;

public interface AuthorizeUseCase {
    AuthorizeUseCaseResponse run(AuthorizeUseCaseRequest request);
}
