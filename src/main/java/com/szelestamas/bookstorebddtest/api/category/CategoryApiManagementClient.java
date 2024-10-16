package com.szelestamas.bookstorebddtest.api.category;

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
public class CategoryApiManagementClient extends ApiManagementClient {
    public CategoryApiManagementClient(RestTemplate restTemplate, ApiServiceProperties apiServiceProperties,
                                       AuthorizationProvider authorizationProvider) {
        super(restTemplate, apiServiceProperties, authorizationProvider);
    }

    public ResponseEntity<List<CategoryResource>> getCategories() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<CategoryResource> getCategory(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id), HttpMethod.GET, null, CategoryResource.class);
    }

    public ResponseEntity<CategoryResource> createCategory(CategoryDto category, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.POST, new HttpEntity<>(category), CategoryResource.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.POST, new HttpEntity<>(category, headers), CategoryResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Void> deleteCategory(long id, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id), HttpMethod.DELETE, null, Void.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<List<BookResource>> getBooksByCategory(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id + "/books"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Map<String, Object>> deleteBooksByCategory(long id, String personaName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id + "/books"), HttpMethod.DELETE, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
    }
}
