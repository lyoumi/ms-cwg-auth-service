package com.cwd.tg.auth.errors.exceptions;

public class TokenExpirationException extends RuntimeException {

    public TokenExpirationException(String message) {
        super(message);
    }

    public TokenExpirationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
