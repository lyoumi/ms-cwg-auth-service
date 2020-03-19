package com.cwd.tg.auth.errors.exceptions;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
