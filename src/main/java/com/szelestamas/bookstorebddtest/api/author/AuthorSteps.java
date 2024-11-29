package com.szelestamas.bookstorebddtest.api.author;

import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class AuthorSteps {
    private final AuthorApiManagementClient apiManagementClient;
    private final RunContext runContext;

    @When("{word} records the author {string}")
    public void userRecordsNewAuthor(String personaName, String name) {
        AuthorDto author = new AuthorDto(runContext.identifierWithRunId(name));
        ResponseEntity<AuthorResource> response = apiManagementClient.createAuthor(author, personaName);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().fullName()));
        assertTrue(response.getHeaders().containsKey("Location"));
        String location = apiManagementClient.getBaseUrl() + "/authors/" + response.getBody().id();
        assertEquals(location, response.getHeaders().getLocation().toString());
        runContext.addCreatedResource(name, response.getBody());
    }

    @Then("{word} can read the previously recorded author {string}")
    public void userCanReadThePreviouslyRecordedAuthor(String personaName, String name) {
        AuthorResource expectedAuthor = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<AuthorResource> response = apiManagementClient.getAuthor(expectedAuthor.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().fullName()));
    }

    @Then("{word} can read the following authors:")
    public void userReceivesAuthors(String personaName, List<String> names) {
        ResponseEntity<List<AuthorResource>> response = apiManagementClient.getAuthors();
        assertEquals(200, response.getStatusCode().value());
        response.getBody().forEach(author -> {
            if (runContext.isAFeatureTestIdentifier(author.fullName()))
                runContext.addCreatedResource(runContext.identifierWithoutRunId(author.fullName()), author);
        });
        assertFalse(runContext.getResourceMap(AuthorResource.class).isEmpty());
        assertThat(names, containsInAnyOrder(runContext.getResourceMap(AuthorResource.class).keySet().toArray()));
    }

    @Then("{word} cannot record the author {string}")
    public void userCannotRecordTheAuthor(String personaName, String name) {
        AuthorDto author = new AuthorDto(runContext.identifierWithRunId(name));
        ResponseEntity<AuthorResource> response =  apiManagementClient.createAuthor(author, personaName);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("{word} updates the previously recorded author's name from {string} to {string}")
    public void userUpdatesThePreviouslyRecordedAuthor(String personaName, String before, String after) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, before);
        runContext.removeCreatedResource(before, AuthorResource.class);
        AuthorDto updatedAuthor = new AuthorDto(runContext.identifierWithRunId(after));
        ResponseEntity<AuthorResource> response = apiManagementClient.updateAuthor(author.id(), updatedAuthor, personaName);
        assertEquals(200, response.getStatusCode().value());
        runContext.addCreatedResource(after, response.getBody());
        assertEquals(after, runContext.identifierWithoutRunId(response.getBody().fullName()));
    }

    @Then("{word} can read the previously modified author {string}")
    public void userCanReadThePreviouslyModifiedAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<AuthorResource> response = apiManagementClient.getAuthor(author.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().fullName()));
    }

    @Then("{word} cannot update the author {string} to {string}")
    public void userCannotUpdateTheAuthor(String personaName, String before, String after) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, before);
        AuthorDto updatedAuthor = new AuthorDto(runContext.identifierWithRunId(after));
        ResponseEntity<AuthorResource> response = apiManagementClient.updateAuthor(author.id(), updatedAuthor, personaName);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("{word} deletes the recorded author {string}")
    public void userDeletesTheRecordedAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteAuthor(author.id(), personaName);
        assertEquals(204, response.getStatusCode().value());

    }

    @Then("{word} cannot read the author {string}")
    public void userCannotReadTheAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<List<AuthorResource>> response = apiManagementClient.getAuthors();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().contains(author));
    }

    @When("{word} cannot delete the author {string}")
    public void userCannotDeleteTheAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteAuthor(author.id(), personaName);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("{word} deletes all books from the author {string}")
    public void userDeletesAllBooksFromTheAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteBooksByAuthor(author.id(), personaName);
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("All books deleted from the author {string}")
    public void allBooksDeletedFromTheAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<List<BookResource>> response = apiManagementClient.getBooksByAuthor(author.id());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Then("{word} doesn't have the rights to record the author {string}")
    public void userDoesntHaveTheRightsToRecordTheAuthor(String personaName, String name) {
        AuthorDto author = new AuthorDto(runContext.identifierWithRunId(name));
        ResponseEntity<AuthorResource> response = apiManagementClient.createAuthor(author, personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to update the previously recorded author's name from {string} to {string}")
    public void userDontHaveTheRightsToUpdateThePreviouslyRecordedAuthorName(String personaName, String before, String after) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, before);
        AuthorDto updatedAuthor = new AuthorDto(runContext.identifierWithRunId(after));
        ResponseEntity<AuthorResource> response = apiManagementClient.updateAuthor(author.id(), updatedAuthor, personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to delete the author {string}")
    public void userDoesntHaveTheRightsToDeleteTheAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteAuthor(author.id(), personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @When("{word} is not able to record the author {string}")
    public void userIsNotAbleToRecordTheAuthor(String personaName, String name) {
        AuthorDto author = new AuthorDto(runContext.identifierWithRunId(name));
        ResponseEntity<AuthorResource> response = apiManagementClient.createAuthor(author, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} is not able to update the previously recorded author's name from {string} to {string}")
    public void userIsNotAbleToUpdateThePreviouslyRecordedAuthorsName(String personaName, String before, String after) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, before);
        AuthorDto updatedAuthor = new AuthorDto(runContext.identifierWithRunId(after));
        ResponseEntity<AuthorResource> response = apiManagementClient.updateAuthor(author.id(), updatedAuthor, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} is not able to delete the author {string}")
    public void userIsNotAbleToDeleteTheAuthor(String personaName, String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteAuthor(author.id(), "");
        assertEquals(401, response.getStatusCode().value());
    }
}
