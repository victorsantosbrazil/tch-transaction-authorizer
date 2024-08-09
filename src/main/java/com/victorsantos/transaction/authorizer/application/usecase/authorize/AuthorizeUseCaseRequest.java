package com.victorsantos.transaction.authorizer.application.usecase.authorize;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthorizeUseCaseRequest {
    private String accountId;
    private BigDecimal totalAmount;
    private String mcc;
    private String merchant;
}
