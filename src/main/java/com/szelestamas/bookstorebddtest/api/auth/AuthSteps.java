package com.szelestamas.bookstorebddtest.api.auth;

import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import com.szelestamas.bookstorebddtest.core.persona.Persona;
import com.szelestamas.bookstorebddtest.core.persona.PersonaProperty;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class AuthSteps {
    private final AuthApiManagementClient apiManagementClient;
    private final RunContext runContext;
    private final PersonaProperty personaProperty;

    @Given("{word} hires {word}")
    public void userHiresUser(String adminName, String personaName) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), StringUtils.capitalize(persona.getType()), persona.getType());
        ResponseEntity<UserResource> response = apiManagementClient.addUser(user, adminName);
        runContext.addCreatedResource(response.getBody().email(), response.getBody());
        assertEquals(201, response.getStatusCode().value());
        assertEquals(user.login(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.lastName(), response.getBody().lastName());
        assertEquals(user.role().toUpperCase(), response.getBody().role());
    }

    @When("{word} sign up and creates a password")
    public void userSignUpAndCreatesAPassword(String personaName) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), StringUtils.capitalize(persona.getType()), persona.getType());
        ResponseEntity<UserResource> response = apiManagementClient.signUp(user);
        runContext.addCreatedResource(response.getBody().email(), response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user.login(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.lastName(), response.getBody().lastName());
        assertEquals(user.role().toUpperCase(), response.getBody().role());
    }

    @Then("{word} can access his/her profile with his/her data")
    public void userCanAccessHisProfileWithHisData(String personaName) {
        ResponseEntity<UserResource> response = apiManagementClient.getProfile(personaName);
        assertEquals(200, response.getStatusCode().value());
        UserResource user = runContext.createdResource(UserResource.class, personaProperty.getPersona(personaName).getEmail());
        assertEquals(user.email(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.role().toUpperCase(), response.getBody().role());
        assertEquals(user.lastName(), response.getBody().lastName());
    }

    @When("{word} updates {word}'s role to {word}")
    public void userUpdatesUserRoleToRole(String adminName, String personaName, String role) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), StringUtils.capitalize(persona.getType()), role);
        ResponseEntity<UserResource> response = apiManagementClient.updateUser(user, adminName);
        runContext.addCreatedResource(response.getBody().email(), response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user.login(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.lastName(), response.getBody().lastName());
        assertEquals(role.toUpperCase(), response.getBody().role());
    }

    @When("{word} updates her/his last name to: {word}")
    public void userUpdatesLastName(String personaName, String lastName) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), lastName, persona.getType());
        ResponseEntity<UserResource> response = apiManagementClient.updateProfile(user, persona.getName());
        runContext.removeCreatedResource(user.login(), UserResource.class);
        runContext.addCreatedResource(response.getBody().email(), response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user.login(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.lastName(), response.getBody().lastName());
        assertEquals(user.role().toUpperCase(), response.getBody().role());
    }

    @When("{word} cannot update her/his role")
    public void userCannotUpdateTheRole(String personaName) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), StringUtils.capitalize(persona.getType()), "Staff");
        ResponseEntity<UserResource> response = apiManagementClient.updateProfile(user, persona.getName());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user.login(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.lastName(), response.getBody().lastName());
        assertNotEquals(user.role().toUpperCase(), response.getBody().role());
    }

    @When("{word} updates {word}'s last name to: {word}")
    public void userUpdatesUsersLastName(String adminName, String personaName, String lastName) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), lastName, persona.getType());
        ResponseEntity<UserResource> response = apiManagementClient.updateUser(user, adminName);
        runContext.removeCreatedResource(user.login(), UserResource.class);
        runContext.addCreatedResource(response.getBody().email(), response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user.login(), response.getBody().email());
        assertEquals(user.firstName(), response.getBody().firstName());
        assertEquals(user.lastName(), response.getBody().lastName());
        assertEquals(user.role().toUpperCase(), response.getBody().role());
    }

    @Then("{word} can get all users")
    public void userCanGetAllUsers(String adminName) {
        ResponseEntity<List<UserResource>> response = apiManagementClient.getUsers(adminName);
        assertEquals(200, response.getStatusCode().value());
        List<UserResource> users = response.getBody();
        assertTrue(response.getBody().containsAll(users));
    }

    @Then("{word} deletes {word}")
    public void userDeletesUser(String adminName, String personaName) {
        Persona persona = personaProperty.getPersona(personaName);
        ResponseEntity<Void> response = apiManagementClient.deleteUser(persona.getEmail(), adminName);
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("{word} cannot access her/his profile")
    public void userCannotAccessProfile(String personaName) {
        ResponseEntity<UserResource> response = apiManagementClient.getProfile(personaName);
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} can get all bookmarks")
    public void userCanGetAllBookmarks(String personaName) {
        ResponseEntity<List<BookResource>> response = apiManagementClient.getAllBookmarks(personaName);
        assertEquals(200, response.getStatusCode().value());
        List<BookResource> bookmarks = response.getBody();
        assertThat(runContext.getResourceMap(BookResource.class).values(), containsInAnyOrder(bookmarks.toArray()));
    }

    @When("{word} cannot get the bookmarks")
    public void userCannotGetTheBookmarks(String personaName) {
        ResponseEntity<List<BookResource>> response = apiManagementClient.getAllBookmarks("");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to get all users")
    public void userDoesntHaveTheRightsToGetAllUsers(String personaName) {
        ResponseEntity<List<UserResource>> response = apiManagementClient.getUsers(personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to delete {word}")
    public void userDoesntHaveTheRightsToDeleteUser(String staffName, String personaName) {
        Persona persona = personaProperty.getPersona(personaName);
        ResponseEntity<Void> response = apiManagementClient.deleteUser(persona.getEmail(), staffName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to update {word}'s last name to: {word}")
    public void userDoesntHaveTheRightsToUpdateUserLastNameTo(String staffName, String personaName, String lastName) {
        Persona persona = personaProperty.getPersona(personaName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), lastName, persona.getType());
        ResponseEntity<UserResource> response = apiManagementClient.updateUser(user, staffName);
        assertEquals(403, response.getStatusCode().value());

    }

    @Then("{word} cannot get all registered users")
    public void userCannotGetAllRegisteredUsers(String personaName) {
        ResponseEntity<List<UserResource>> response = apiManagementClient.getUsers("");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} cannot delete {word}")
    public void userCannotDeleteUser(String personaName, String userName) {
        Persona user = personaProperty.getPersona(userName);
        ResponseEntity<Void> response = apiManagementClient.deleteUser(user.getEmail(), "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} cannot update {word}'s last name to: {word}")
    public void userCannotUpdateSuesLastNameTo(String personaName, String userName, String lastName) {
        Persona persona = personaProperty.getPersona(userName);
        UserDto user = new UserDto(persona.getEmail(), persona.getPassword(), runContext.identifierWithRunId(persona.getName()), lastName, persona.getType());
        ResponseEntity<UserResource> response = apiManagementClient.updateUser(user, "");
        assertEquals(401, response.getStatusCode().value());
    }
}
