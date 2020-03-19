package com.cwd.tg.auth.errors.exceptions;

public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException(String message) {
        super(message);
    }
}
