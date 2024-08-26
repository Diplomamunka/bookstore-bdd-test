package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.ApiServiceProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BookApiManagementClient extends ApiManagementClient {
    BookApiManagementClient(RestTemplate restTemplate, ApiServiceProperties apiServiceProperties) {
        super(restTemplate, apiServiceProperties);
    }

    public ResponseEntity<BookResource> getBook(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id), HttpMethod.GET, null, BookResource.class);
    }

    public ResponseEntity<List<BookResource>> getBooks() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<BookResource> updateBook(long id, BookDto book) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id), HttpMethod.PUT, new HttpEntity<>(book), BookResource.class);
    }

    public ResponseEntity<Void> deleteBook(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id), HttpMethod.DELETE, null, Void.class);
    }

    public ResponseEntity<BookResource> createBook(BookDto book) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books"), HttpMethod.PUT, new HttpEntity<>(book), BookResource.class);
    }
}
