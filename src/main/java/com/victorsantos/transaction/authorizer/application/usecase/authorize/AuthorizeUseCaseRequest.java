package com.victorsantos.transaction.authorizer.application.usecase.authorize;

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
    @NotBlank
    private String account;

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    private String mcc;

    private String merchant;
}
