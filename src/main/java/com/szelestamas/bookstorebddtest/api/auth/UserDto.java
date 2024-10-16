package com.szelestamas.bookstorebddtest.api.auth;

public record UserDto(String login, String password, String firstName, String lastName, String role) {
}
