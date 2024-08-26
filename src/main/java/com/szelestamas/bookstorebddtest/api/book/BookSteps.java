package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.author.AuthorDto;
import com.szelestamas.bookstorebddtest.api.author.AuthorResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
public class BookSteps {
    private final RunContext runContext;
    private final BookApiManagementClient apiManagementClient;

    @When("User records the following book with required values:")
    public void userRecordsTheBook(BookDto book) {
        book.setTitle(runContext.identifierWithRunId(book.getTitle()));
        book.getAuthors().forEach(author -> author.setFullName(runContext.identifierWithRunId(author.getFullName())));
        ResponseEntity<BookResource> response = apiManagementClient.createBook(book);
        runContext.addCreatedResource(runContext.identifierWithoutRunId(response.getBody().getTitle()), response.getBody());
        response.getBody().getAuthors().forEach(author -> runContext.addCreatedResource(runContext.identifierWithoutRunId(author.fullName()), author));
        assertEquals(201, response.getStatusCode().value());
        assertTrue(response.getHeaders().containsKey("Location"));
        String location = apiManagementClient.getApplicationUrl() + "/books/" + response.getBody().getId();
        assertEquals(location, response.getHeaders().getLocation().toString());
        assertEquals(book.getTitle(), response.getBody().getTitle());
        assertEquals(book.getDiscount(), response.getBody().getDiscount());
        assertEquals(book.getPrice(), response.getBody().getPrice());
        assertEquals(book.isAvailable(), response.getBody().isAvailable());
        assertEquals(book.getCategory(), response.getBody().getCategory());
        assertThat(book.getAuthors().stream().map(AuthorDto::getFullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
    }

    @Then("User can read the previously recorded book {string}")
    public void userCanReadThePreviouslyCreatedBook(String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<BookResource> response = apiManagementClient.getBook(book.getId());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(book.getTitle(), response.getBody().getTitle());
        assertEquals(book.getDiscount(), response.getBody().getDiscount());
        assertEquals(book.getPrice(), response.getBody().getPrice());
        assertEquals(book.isAvailable(), response.getBody().isAvailable());
        assertEquals(book.getCategory(), response.getBody().getCategory());
        assertThat(book.getAuthors().stream().map(AuthorResource::fullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
    }
}
