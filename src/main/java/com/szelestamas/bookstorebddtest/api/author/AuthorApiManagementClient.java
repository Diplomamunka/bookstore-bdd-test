package com.szelestamas.bookstorebddtest.api.author;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.ApiServiceProperties;
import com.szelestamas.bookstorebddtest.api.book.BookResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AuthorApiManagementClient extends ApiManagementClient {
    AuthorApiManagementClient(ApiServiceProperties apiServiceProperties, RestTemplate restTemplate) {
        super(restTemplate, apiServiceProperties);
    }

    public ResponseEntity<AuthorResource> createAuthor(AuthorDto author) {
        try {
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors"), HttpMethod.PUT, new HttpEntity<>(author), AuthorResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<List<AuthorResource>> getAuthors() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Void> deleteAuthor(long id) {
        try {
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.DELETE, null, Void.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<AuthorResource> getAuthor(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.GET, null, AuthorResource.class);
    }

    public ResponseEntity<AuthorResource> updateAuthor(long id, AuthorDto author) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.PUT, new HttpEntity<>(author), AuthorResource.class);
    }

    public ResponseEntity<List<BookResource>> getBooksByAuthor(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id + "/books"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Map<String, Object>> deleteBooksByAuthor(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id + "/books"), HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});
    }
}
