package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class AuthorizeUseCaseRequest {

    @Schema(example = "123", description = "An identifier for the account")
    @NotBlank
    private String account;

    @Schema(example = "100.00", description = "The amount to be debited from a balance")
    @NotNull
    @Positive
    private BigDecimal totalAmount;

    @Schema(
            example = "5811",
            description =
                    "A 4-digit numeric code that classifies establishments commercial according to the type of product sold or service provided")
    private String mcc;

    @Schema(example = "PADARIA DO ZE SAO PAULO BR", description = "The name of the establishment")
    private String merchant;
}
