package com.szelestamas.bookstorebddtest.api.category;

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
public class CategoryApiManagementClient extends ApiManagementClient {
    public CategoryApiManagementClient(RestClient restClient,
                                       AuthorizationProvider authorizationProvider, String baseUrl) {
        super(baseUrl, restClient, authorizationProvider);
    }

    public ResponseEntity<List<CategoryResource>> getCategories() {
        return restClient.get().uri("/categories").retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<CategoryResource> getCategory(long id) {
        return restClient.get().uri("/categories/{id}", id).retrieve().toEntity(CategoryResource.class);
    }

    public ResponseEntity<CategoryResource> createCategory(CategoryDto category, String personaName) {
        var baseRequest = restClient.post().uri("/categories").body(category);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(CategoryResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(CategoryResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<CategoryResource> updateCategory(long id, CategoryDto category, String personaName) {
        var baseRequest = restClient.put().uri("/categories/{id}", id).body(category);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(CategoryResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(CategoryResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Void> deleteCategory(long id, String personaName) {
        var baseRequest = restClient.delete().uri("/categories/{id}", id);
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

    public ResponseEntity<List<BookResource>> getBooksByCategory(long id) {
        return restClient.get().uri("/categories/{id}/books", id).retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Void> deleteBooksByCategory(long id, String personaName) {
        return restClient.delete().uri("/categories/{id}/books", id)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                .retrieve().toBodilessEntity();
    }
}
