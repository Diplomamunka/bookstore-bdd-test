package com.szelestamas.bookstorebddtest.api.author;

import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class AuthorSteps {
    private final AuthorApiManagementClient apiManagementClient;
    private final RunContext runContext;

    @When("User records the author {string}")
    public void userRecordsNewAuthor(String name) {
        AuthorDto author = new AuthorDto(runContext.identifierWithRunId(name));
        ResponseEntity<AuthorResource> response = apiManagementClient.createAuthor(author);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().fullName()));
        assertTrue(response.getHeaders().containsKey("Location"));
        String location = apiManagementClient.getApplicationUrl() + "/authors/" + response.getBody().id();
        assertEquals(location, response.getHeaders().getLocation().toString());
        runContext.addCreatedResource(name, response.getBody());
    }

    @Then("User can read the following authors:")
    public void userReceivesAuthors(List<String> names) {
        ResponseEntity<List<AuthorResource>> response = apiManagementClient.getAuthors();
        assertEquals(200, response.getStatusCode().value());
        response.getBody().forEach(author -> {
            if (runContext.isAFeatureTestIdentifier(author.fullName()))
                runContext.addCreatedResource(runContext.identifierWithoutRunId(author.fullName()), author);
        });
        assertFalse(runContext.getResourceMap(AuthorResource.class).isEmpty());
        assertThat(names, containsInAnyOrder(runContext.getResourceMap(AuthorResource.class).keySet().toArray()));
    }

    @Then("User can read the previously recorded author {string}")
    public void userCanReadThePreviouslyRecordedAuthor(String name) {
        AuthorResource expectedAuthor = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<AuthorResource> response = apiManagementClient.getAuthor(expectedAuthor.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().fullName()));
    }

    @Then("User cannot record the author {string}")
    public void userCannotRecordTheAuthor(String name) {
        AuthorDto author = new AuthorDto(runContext.identifierWithRunId(name));
        ResponseEntity<AuthorResource> response =  apiManagementClient.createAuthor(author);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("User updates the previously recorded author's name from {string} to {string}")
    public void userUpdatesThePreviouslyRecordedAuthor(String before, String after) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, before);
        runContext.removeCreatedResource(before, AuthorResource.class);
        AuthorDto updatedAuthor = new AuthorDto(runContext.identifierWithRunId(after));
        ResponseEntity<AuthorResource> response = apiManagementClient.updateAuthor(author.id(), updatedAuthor);
        runContext.addCreatedResource(after, response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(after, runContext.identifierWithoutRunId(response.getBody().fullName()));
    }

    @Then("User can read the previously modified author {string}")
    public void userCanReadThePreviouslyModifiedAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<AuthorResource> response = apiManagementClient.getAuthor(author.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().fullName()));
    }

    @Then("User cannot update the author {string} to {string}")
    public void userCannotUpdateTheAuthor(String before, String after) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, before);
        AuthorDto updatedAuthor = new AuthorDto(runContext.identifierWithRunId(after));
        ResponseEntity<AuthorResource> response = apiManagementClient.updateAuthor(author.id(), updatedAuthor);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("User deletes the recorded author {string}")
    public void userDeletesTheRecordedAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteAuthor(author.id());
        assertEquals(204, response.getStatusCode().value());

    }

    @Then("User cannot read the author {string}")
    public void userCannotReadTheAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<List<AuthorResource>> response = apiManagementClient.getAuthors();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().contains(author));
    }

    @When("User cannot delete the author {string}")
    public void userCannotDeleteTheAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteAuthor(author.id());
        assertEquals(409, response.getStatusCode().value());
    }

    @When("User deletes all books from the author {string}")
    public void userDeletesAllBooksFromTheAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<Map<String, Object>> response = apiManagementClient.deleteBooksByAuthor(author.id());
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("All books deleted from the author {string}")
    public void allBooksDeletedFromTheAuthor(String name) {
        AuthorResource author = runContext.createdResource(AuthorResource.class, name);
        ResponseEntity<List<BookResource>> response = apiManagementClient.getBooksByAuthor(author.id());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}
