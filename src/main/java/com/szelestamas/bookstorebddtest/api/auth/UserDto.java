package com.szelestamas.bookstorebddtest.api.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(@JsonProperty("email") String login, String password, String firstName, String lastName, String role) {
}
