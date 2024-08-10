package com.victorsantos.transaction.authorizer.application.controller;

import static com.victorsantos.transaction.authorizer.application.controller.ControllerPath.TRANSACTIONS_PATH;

import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseRequest;
import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transactions")
@RequestMapping(value = TRANSACTIONS_PATH)
public interface AuthorizeController {

    @PostMapping
    AuthorizeUseCaseResponse authorize(@RequestBody AuthorizeUseCaseRequest request);
}
