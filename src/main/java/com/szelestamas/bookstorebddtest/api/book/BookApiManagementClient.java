package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.core.authorization.AuthorizationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;
import java.util.List;

@Component
public class BookApiManagementClient extends ApiManagementClient {
    @Value("${file-location}")
    String filesPath;

    BookApiManagementClient(RestClient restClient, String baseUrl,
                            AuthorizationProvider authorizationProvider) {
        super(baseUrl, restClient, authorizationProvider);
    }

    public ResponseEntity<BookResource> getBook(long id) {
        return restClient.get().uri("/books/{id}", id).retrieve().toEntity(BookResource.class);
    }

    public ResponseEntity<List<BookResource>> getBooks() {
        return restClient.get().uri("/books").retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<BookResource> updateBook(long id, BookDto book, String personaName) {
        var baseRequest = restClient.put().uri("/books/{id}", id).body(book);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(BookResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(BookResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Void> deleteBook(long id, String personaName) {
        var baseRequest = restClient.delete().uri("/books/{id}", id);
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

    public ResponseEntity<BookResource> createBook(BookDto book, String personaName) {
        var baseRequest = restClient.post().uri("/books").body(book);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(BookResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(BookResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<String> uploadImage(long id, Path file, String personaName) {
        MultiValueMap<String, Object> files = new LinkedMultiValueMap<>();
        files.add("image", new FileSystemResource(file));
        var baseRequest = restClient.post().uri("/books/{id}/image", id).body(files).headers(httpHeaders -> httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA));
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(String.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Resource> downloadImage(long id) {
        return restClient.get().uri("/books/{id}/image", id).retrieve().toEntity(Resource.class);
    }

    public ResponseEntity<List<BookResource>> addToBookmark(long id, String personaName) {
        var baseRequest = restClient.post().uri("/books/{id}/bookmark", id);
        if (personaName.isEmpty()) {
            try {
                return baseRequest.retrieve().toEntity(new ParameterizedTypeReference<>() {});
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).build();
            }
        }
        return baseRequest
                .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Void> deleteBookmark(long id, String personaName) {
        var baseRequest = restClient.delete().uri("/books/{id}/bookmark", id);
        if (personaName.isEmpty()) {
            try {
                return baseRequest.retrieve().toBodilessEntity();
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).build();
            }
        }
        return baseRequest
                .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                .retrieve().toBodilessEntity();
    }
}
