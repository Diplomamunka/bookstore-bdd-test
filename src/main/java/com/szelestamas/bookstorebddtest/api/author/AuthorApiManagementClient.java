package com.szelestamas.bookstorebddtest.api.author;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.core.authorization.AuthorizationProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class AuthorApiManagementClient extends ApiManagementClient {
    AuthorApiManagementClient(String baseUrl, RestClient restClient,
                              AuthorizationProvider authorizationProvider) {
        super(baseUrl, restClient, authorizationProvider);
    }

    public ResponseEntity<AuthorResource> getAuthor(long id) {
        return restClient.get().uri("/authors/{id}", id).retrieve().toEntity(AuthorResource.class);
    }

    public ResponseEntity<List<AuthorResource>> getAuthors() {
        return restClient.get().uri("/authors").retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<List<BookResource>> getBooksByAuthor(long id) {
        return restClient.get().uri("/authors/{id}/books", id).retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<AuthorResource> createAuthor(AuthorDto author, String personaName) {
        var baseRequest = restClient.post().uri("/authors").body(author);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(AuthorResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(AuthorResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<AuthorResource> updateAuthor(long id, AuthorDto author, String personaName) {
        var baseRequest = restClient.put().uri("/authors/{id}", id).body(author);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(AuthorResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(AuthorResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Void> deleteAuthor(long id, String personaName) {
        var baseRequest = restClient.delete().uri("/authors/{id}", id);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toBodilessEntity();
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toBodilessEntity();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Void> deleteBooksByAuthor(long id, String personaName) {
        return restClient.delete().uri("/authors/{id}/books", id)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                .retrieve().toBodilessEntity();
    }
}
