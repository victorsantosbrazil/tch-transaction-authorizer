package com.victorsantos.transaction.authorizer.application.controller;

import static com.victorsantos.transaction.authorizer.application.controller.ControllerPath.TRANSACTIONS_PATH;

import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseRequest;
import com.victorsantos.transaction.authorizer.application.usecase.authorize.AuthorizeUseCaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transactions")
@RequestMapping(value = TRANSACTIONS_PATH)
public interface AuthorizeController {

    @Operation(
            summary = "Authorize a transaction",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content =
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = AuthorizeUseCaseRequest.class))))
    @PostMapping
    AuthorizeUseCaseResponse authorize(HttpServletRequest httpServletRequest);
}
