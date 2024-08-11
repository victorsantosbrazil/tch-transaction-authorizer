package com.victorsantos.transaction.authorizer.application.exception;

import lombok.Getter;

@Getter
public class AuthorizeTransactionException extends RuntimeException {

    private final String code;

    public AuthorizeTransactionException(String message, String code) {
        super(message);
        this.code = code;
    }
}
