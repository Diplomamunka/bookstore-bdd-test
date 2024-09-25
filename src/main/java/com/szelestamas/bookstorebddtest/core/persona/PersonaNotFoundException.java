package com.szelestamas.bookstorebddtest.core.persona;

public class PersonaNotFoundException extends RuntimeException {
    public PersonaNotFoundException(String message) {
        super(message);
    }
}
