package com.victorsantos.transaction.authorizer.application.usecase.authorize;

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
    private String account;
    private BigDecimal totalAmount;
    private String mcc;
    private String merchant;
}
