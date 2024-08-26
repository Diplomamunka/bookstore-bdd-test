package com.szelestamas.bookstorebddtest.api;

import com.szelestamas.bookstorebddtest.api.author.AuthorApiManagementClient;
import com.szelestamas.bookstorebddtest.api.author.AuthorResource;
import com.szelestamas.bookstorebddtest.api.book.BookApiManagementClient;
import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.api.category.CategoryApiManagementClient;
import com.szelestamas.bookstorebddtest.api.category.CategoryResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BackgroundSteps {
    private final RunContext runContext;
    private final CategoryApiManagementClient categoryApiManagementClient;
    private final AuthorApiManagementClient authorApiManagementClient;
    private final BookApiManagementClient bookApiManagementClient;

    @Given("User data is deleted")
    public void contextIsDeleted() {
        List<BookResource> books = bookApiManagementClient.getBooks().getBody();
        books.stream()
                .filter(book -> runContext.isAFeatureTestIdentifier(book.getTitle()))
                .forEach(book -> bookApiManagementClient.deleteBook(book.getId()));
        List<AuthorResource> authors = authorApiManagementClient.getAuthors().getBody();
        authors.stream()
                .filter(author -> runContext.isAFeatureTestIdentifier(author.fullName()))
                .forEach(author -> {authorApiManagementClient.deleteAuthor(author.id());});
        List<CategoryResource> categories = categoryApiManagementClient.getCategories().getBody();
        categories.stream()
                .filter(category -> runContext.isAFeatureTestIdentifier(category.name()))
                .forEach(category -> categoryApiManagementClient.deleteCategory(category.id()));
        runContext.resetCreatedResources();
        runContext.setHttpStatus(null);
    }

}
