package com.szelestamas.bookstorebddtest.api;

import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class BookSteps {
    private final RunContext runContext;
    private final ApiManagementClient apiManagementClient;

    @When("User records the following book:")
    public void userRecordsTheBook(Book book) {
        book.setTitle(name);
        ResponseEntity<Book> response = apiManagementClient.createBook(book);
    }
}
