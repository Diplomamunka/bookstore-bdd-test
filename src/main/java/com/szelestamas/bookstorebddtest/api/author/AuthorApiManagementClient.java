package com.szelestamas.bookstorebddtest.api.author;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.ApiServiceProperties;
import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.core.authorization.AuthorizationProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AuthorApiManagementClient extends ApiManagementClient {
    AuthorApiManagementClient(ApiServiceProperties apiServiceProperties, RestTemplate restTemplate,
                              AuthorizationProvider authorizationProvider) {
        super(restTemplate, apiServiceProperties, authorizationProvider);
    }

    public ResponseEntity<AuthorResource> createAuthor(AuthorDto author, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors"), HttpMethod.POST, new HttpEntity<>(author), AuthorResource.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors"), HttpMethod.POST, new HttpEntity<>(author, headers), AuthorResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<List<AuthorResource>> getAuthors() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Void> deleteAuthor(long id, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.DELETE, null, Void.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<AuthorResource> getAuthor(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.GET, null, AuthorResource.class);
    }

    public ResponseEntity<AuthorResource> updateAuthor(long id, AuthorDto author, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.PUT, new HttpEntity<>(author), AuthorResource.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id), HttpMethod.PUT, new HttpEntity<>(author, headers), AuthorResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<List<BookResource>> getBooksByAuthor(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id + "/books"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Map<String, Object>> deleteBooksByAuthor(long id, String personaName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/authors/" + id + "/books"), HttpMethod.DELETE, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
    }
}
