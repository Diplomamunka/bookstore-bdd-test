package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.author.AuthorApiManagementClient;
import com.szelestamas.bookstorebddtest.api.author.AuthorDto;
import com.szelestamas.bookstorebddtest.api.author.AuthorResource;
import com.szelestamas.bookstorebddtest.api.category.CategoryDto;
import com.szelestamas.bookstorebddtest.api.category.CategoryResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@RequiredArgsConstructor
public class BookSteps {
    private final RunContext runContext;
    private final BookApiManagementClient apiManagementClient;
    private final AuthorApiManagementClient authorApiManagementClient;

    @When("{word} records the following book:")
    public void userRecordsTheBook(String personaName, BookDto book) {
        book.setTitle(runContext.identifierWithRunId(book.getTitle()));
        if (runContext.getResourceMap(CategoryResource.class).containsKey(book.getCategory().name()))
            book.setCategory(new CategoryDto(runContext.identifierWithRunId(book.getCategory().name())));
        book.getAuthors().forEach(author -> author.setFullName(runContext.identifierWithRunId(author.getFullName())));
        ResponseEntity<BookResource> response = apiManagementClient.createBook(book, personaName);
        runContext.addCreatedResource(runContext.identifierWithoutRunId(response.getBody().getTitle()), response.getBody());
        response.getBody().getAuthors().forEach(author -> {
            if (!runContext.getResourceMap(AuthorResource.class).containsKey(runContext.identifierWithoutRunId(author.fullName())))
                runContext.addCreatedResource(runContext.identifierWithoutRunId(author.fullName()), author);
        });
        assertEquals(201, response.getStatusCode().value());
        assertTrue(response.getHeaders().containsKey("Location"));
        String location = apiManagementClient.getBaseUrl() + "/books/" + response.getBody().getId();
        assertEquals(location, response.getHeaders().getLocation().toString());
        assertEquals(book.getTitle(), response.getBody().getTitle());
        assertEquals(book.getDiscount(), response.getBody().getDiscount());
        assertEquals(book.getPrice(), response.getBody().getPrice());
        assertEquals(book.isAvailable(), response.getBody().isAvailable());
        assertEquals(book.getCategory().name(), response.getBody().getCategory().name());
        assertEquals(book.getShortDescription(), response.getBody().getShortDescription());
        assertEquals(book.getReleaseDate(), response.getBody().getReleaseDate());
        assertThat(book.getAuthors().stream().map(AuthorDto::getFullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
        assertThat(book.getTags(), containsInAnyOrder(response.getBody().getTags().toArray()));
    }

    @Then("{word} can read the previously recorded book {string}")
    public void userCanReadThePreviouslyCreatedBook(String personaName, String title) {
        BookResource expectedBook = runContext.createdResource(BookResource.class, title);
        ResponseEntity<BookResource> response = apiManagementClient.getBook(expectedBook.getId());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedBook.getTitle(), response.getBody().getTitle());
        assertEquals(expectedBook.getDiscount(), response.getBody().getDiscount());
        assertEquals(expectedBook.getPrice(), response.getBody().getPrice());
        assertEquals(expectedBook.isAvailable(), response.getBody().isAvailable());
        assertEquals(expectedBook.getCategory().name(), response.getBody().getCategory().name());
        assertThat(expectedBook.getAuthors().stream().map(AuthorResource::fullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
        assertEquals(expectedBook.getShortDescription(), response.getBody().getShortDescription());
        assertEquals(expectedBook.getReleaseDate(), response.getBody().getReleaseDate());
        assertThat(expectedBook.getTags(), containsInAnyOrder(response.getBody().getTags().toArray()));
    }

    @When("{word} uploads an image of the book {string}")
    public void userUploadsAnImageOfTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        Path file = Path.of(apiManagementClient.filesPath, "egri_csillagok.jpg");
        ResponseEntity<String> response = apiManagementClient.uploadImage(book.getId(), file, personaName);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("File uploaded successfully: egri_csillagok.jpg", response.getBody());
    }

    @Then("{word} can download the image of the book {string}")
    public void userCanDownloadTheImageOfTheBook(String personaName, String title) throws IOException {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<Resource> response = apiManagementClient.downloadImage(book.getId());
        assertEquals(200, response.getStatusCode().value());
        assertEquals("egri_csillagok.jpg", response.getBody().getFilename());
        byte[] file = Files.readAllBytes(Path.of(apiManagementClient.filesPath, "egri_csillagok.jpg"));
        assertArrayEquals(file, response.getBody().getContentAsByteArray());
    }

    @Then("{word} can read the following books:")
    public void userCanReadTheFollowingBooks(String personaName, List<String> titles) {
        ResponseEntity<List<BookResource>> response = apiManagementClient.getBooks();
        assertEquals(200, response.getStatusCode().value());
        response.getBody().forEach(book -> {
            if (runContext.isAFeatureTestIdentifier(book.getTitle()))
                runContext.addCreatedResource(runContext.identifierWithoutRunId(book.getTitle()), book);
        });
        assertFalse(runContext.getResourceMap(BookResource.class).isEmpty());
        assertThat(titles, containsInAnyOrder(runContext.getResourceMap(BookResource.class).keySet().toArray()));
    }

    @When("{word} adds the following description to book {string}: {string}")
    public void userAddsADescriptionToBook(String personaName, String title, String description) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        runContext.removeCreatedResource(title, BookResource.class);
        BookDto updatedBook = new BookDto();
        updatedBook.setTitle(book.getTitle());
        updatedBook.setDiscount(book.getDiscount());
        updatedBook.setPrice(book.getPrice());
        updatedBook.setAvailable(book.isAvailable());
        updatedBook.setCategory(new CategoryDto(book.getCategory().name()));
        updatedBook.setReleaseDate(book.getReleaseDate());
        updatedBook.setAuthors(book.getAuthors().stream().map(author -> new AuthorDto(author.fullName())).toList());
        updatedBook.setShortDescription(description);
        updatedBook.setTags(book.getTags());
        ResponseEntity<BookResource> response = apiManagementClient.updateBook(book.getId(), updatedBook, personaName);
        runContext.addCreatedResource(title, response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedBook.getTitle(), response.getBody().getTitle());
        assertEquals(updatedBook.getDiscount(), response.getBody().getDiscount());
        assertEquals(updatedBook.getPrice(), response.getBody().getPrice());
        assertEquals(updatedBook.isAvailable(), response.getBody().isAvailable());
        assertEquals(updatedBook.getCategory().name(), response.getBody().getCategory().name());
        assertEquals(updatedBook.getReleaseDate(), response.getBody().getReleaseDate());
        assertEquals(updatedBook.getShortDescription(), response.getBody().getShortDescription());
        assertThat(updatedBook.getAuthors().stream().map(AuthorDto::getFullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
        assertThat(updatedBook.getTags(), containsInAnyOrder(response.getBody().getTags().toArray()));
    }

    @Then("{word} can read the previously modified book {string}")
    public void userCanReadThePreviouslyModifiedBook(String personaName, String title) {
        BookResource expectedBook = runContext.createdResource(BookResource.class, title);
        ResponseEntity<BookResource> response = apiManagementClient.getBook(expectedBook.getId());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedBook.getTitle(), response.getBody().getTitle());
        assertEquals(expectedBook.getDiscount(), response.getBody().getDiscount());
        assertEquals(expectedBook.getPrice(), response.getBody().getPrice());
        assertEquals(expectedBook.isAvailable(), response.getBody().isAvailable());
        assertEquals(expectedBook.getShortDescription(), response.getBody().getShortDescription());
        assertEquals(expectedBook.getReleaseDate(), response.getBody().getReleaseDate());
        assertEquals(expectedBook.getCategory().name(), response.getBody().getCategory().name());
        assertThat(expectedBook.getAuthors().stream().map(AuthorResource::fullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
        assertThat(expectedBook.getTags(), containsInAnyOrder(response.getBody().getTags().toArray()));
    }

    @When("{word} changes the category of the book {string} to {string}")
    public void userChangesTheCategoryOfTheBook(String personaName, String title, String category) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        runContext.removeCreatedResource(title, BookResource.class);
        BookDto updatedBook = new BookDto();
        updatedBook.setTitle(book.getTitle());
        updatedBook.setDiscount(book.getDiscount());
        updatedBook.setPrice(book.getPrice());
        updatedBook.setAvailable(book.isAvailable());
        updatedBook.setCategory(CategoryDto.parse(category));
        updatedBook.setReleaseDate(book.getReleaseDate());
        updatedBook.setAuthors(book.getAuthors().stream().map(author -> new AuthorDto(author.fullName())).toList());
        updatedBook.setShortDescription(book.getShortDescription());
        updatedBook.setTags(book.getTags());
        ResponseEntity<BookResource> response = apiManagementClient.updateBook(book.getId(), updatedBook, personaName);
        runContext.addCreatedResource(title, response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedBook.getTitle(), response.getBody().getTitle());
        assertEquals(updatedBook.getDiscount(), response.getBody().getDiscount());
        assertEquals(updatedBook.getPrice(), response.getBody().getPrice());
        assertEquals(updatedBook.isAvailable(), response.getBody().isAvailable());
        assertEquals(updatedBook.getCategory().name(), response.getBody().getCategory().name());
        assertEquals(updatedBook.getReleaseDate(), response.getBody().getReleaseDate());
        assertEquals(updatedBook.getShortDescription(), response.getBody().getShortDescription());
        assertThat(updatedBook.getAuthors().stream().map(AuthorDto::getFullName).toList(),
                containsInAnyOrder(response.getBody().getAuthors().stream().map(AuthorResource::fullName).toArray()));
        assertThat(updatedBook.getTags(), containsInAnyOrder(response.getBody().getTags().toArray()));
    }

    @When("{word} deletes the recorded book {string}")
    public void userDeletesTheRecordedBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<Void> response = apiManagementClient.deleteBook(book.getId(), personaName);
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("{word} cannot read the book {string}")
    public void userCannotReadTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<List<BookResource>> response = apiManagementClient.getBooks();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().contains(book));
    }

    @When("{word} can read the previously recorded book {string} in the books of {string}")
    public void userCanReadThePreviouslyRecordedBookInTheBooksOfAuthor(String personaName, String title, String author) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        AuthorResource authorResource = runContext.createdResource(AuthorResource.class, author);
        ResponseEntity<List<BookResource>> response = authorApiManagementClient.getBooksByAuthor(authorResource.id());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains(book));
    }

    @When("{word} cannot upload the big image to the book {string}")
    public void userCannotUploadTheBigImageToTheBookH(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        Path file = Path.of(apiManagementClient.filesPath, "big_image.jpg");
        ResponseEntity<String> response = apiManagementClient.uploadImage(book.getId(), file, personaName);
        assertEquals(413, response.getStatusCode().value());
    }

    @Then("{word} can read the previously recorded book {string} in the books of")
    public void userCanReadThePreviouslyRecordedBookInTheBooksOf(String personaName, String title, List<String> authorNames) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        List<AuthorResource> authors = authorNames.stream()
                .map(authorName -> runContext.createdResource(AuthorResource.class, authorName)).toList();
        authors.forEach(author -> {
            ResponseEntity<List<BookResource>> response = authorApiManagementClient.getBooksByAuthor(author.id());
            assertEquals(200, response.getStatusCode().value());
            assertTrue(response.getBody().contains(book));
        });
    }

    @Then("{word} bookmarks the book: {string}")
    public void userBookmarksTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<List<BookResource>> response = apiManagementClient.addToBookmark(book.getId(), personaName);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains(book));
    }

    @Then("{word} can delete the bookmark for {string}")
    public void userDeleteTheBookmark(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<Void> response = apiManagementClient.deleteBookmark(book.getId(), personaName);
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("{word} cannot bookmark the book: {string}")
    public void userCannotBookmarkTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<List<BookResource>> response = apiManagementClient.addToBookmark(book.getId(), "");
        assertEquals(401, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Then("{word} doesn't have the rights to record the following book:")
    public void userDoesntHaveTheRightsToRecordTheFollowingBook(String personaName, BookDto book) {
        ResponseEntity<BookResource> response = apiManagementClient.createBook(book, personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to upload image to the book {string}")
    public void userDoesntHaveTheRightsToUploadImageToTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        Path file = Path.of(apiManagementClient.filesPath, "egri_csillagok.jpg");
        ResponseEntity<String> response = apiManagementClient.uploadImage(book.getId(), file, personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to update the description of the book {string}: {string}")
    public void userDoesntHaveTheRightsToUpdateTheDescriptionOfTheBook(String personaName, String title, String description) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        runContext.removeCreatedResource(title, BookResource.class);
        BookDto updatedBook = new BookDto();
        updatedBook.setTitle(book.getTitle());
        updatedBook.setDiscount(book.getDiscount());
        updatedBook.setPrice(book.getPrice());
        updatedBook.setAvailable(book.isAvailable());
        updatedBook.setCategory(new CategoryDto(book.getCategory().name()));
        updatedBook.setReleaseDate(book.getReleaseDate());
        updatedBook.setAuthors(book.getAuthors().stream().map(author -> new AuthorDto(author.fullName())).toList());
        updatedBook.setShortDescription(description);
        updatedBook.setTags(book.getTags());
        ResponseEntity<BookResource> response = apiManagementClient.updateBook(book.getId(), updatedBook, personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to delete the book {string}")
    public void userDoesntHaveTheRightsToDeleteTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<Void> response = apiManagementClient.deleteBook(book.getId(), personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} cannot delete the bookmark for {string}")
    public void userCannotDeleteTheBookmarkFor(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<Void> response = apiManagementClient.deleteBookmark(book.getId(), "");
        assertEquals(401, response.getStatusCode().value());
    }

    @When("{word} cannot record the following book:")
    public void userCannotRecordTheFollowingBook(String personaName, BookDto book) {
        ResponseEntity<BookResource> response = apiManagementClient.createBook(book, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} cannot upload image to the book {string}")
    public void userCannotUploadImageToTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        Path file = Path.of(apiManagementClient.filesPath, "egri_csillagok.jpg");
        ResponseEntity<String> response = apiManagementClient.uploadImage(book.getId(), file, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} cannot update the description of the book {string}: {string}")
    public void userCannotUpdateTheDescriptionOfTheBook(String personaName, String title, String description) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        runContext.removeCreatedResource(title, BookResource.class);
        BookDto updatedBook = new BookDto();
        updatedBook.setTitle(book.getTitle());
        updatedBook.setDiscount(book.getDiscount());
        updatedBook.setPrice(book.getPrice());
        updatedBook.setAvailable(book.isAvailable());
        updatedBook.setCategory(new CategoryDto(book.getCategory().name()));
        updatedBook.setReleaseDate(book.getReleaseDate());
        updatedBook.setAuthors(book.getAuthors().stream().map(author -> new AuthorDto(author.fullName())).toList());
        updatedBook.setShortDescription(description);
        updatedBook.setTags(book.getTags());
        ResponseEntity<BookResource> response = apiManagementClient.updateBook(book.getId(), updatedBook, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} cannot delete the book {string}")
    public void userCannotDeleteTheBook(String personaName, String title) {
        BookResource book = runContext.createdResource(BookResource.class, title);
        ResponseEntity<Void> response = apiManagementClient.deleteBook(book.getId(), "");
        assertEquals(401, response.getStatusCode().value());
    }
}
