package com.szelestamas.bookstorebddtest.api.auth;

import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthSteps {
    private final AuthApiManagementClient apiManagementClient;
    private final RunContext runContext;

    @When("{word} deletes a book")
    public void login(String personaName) {
        apiManagementClient.signIn(personaName);
    }
}
