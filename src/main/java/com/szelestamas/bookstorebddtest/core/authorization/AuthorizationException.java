package com.szelestamas.bookstorebddtest.core.authorization;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}
