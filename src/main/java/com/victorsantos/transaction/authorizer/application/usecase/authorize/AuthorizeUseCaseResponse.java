package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthorizeUseCaseResponse {

    @Schema(
            example = "00",
            description =
                    """
                    Authorization code. Possible values are:
                      00 - transaction approved
                      51 - transaction refused
                      07 - problem that prevents the transaction from being processed
                    """)
    private String code;
}
