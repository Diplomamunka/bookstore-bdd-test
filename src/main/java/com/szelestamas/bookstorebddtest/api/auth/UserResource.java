package com.szelestamas.bookstorebddtest.api.auth;

public record UserResource(String email, String firstName, String lastName, String role) {
}
