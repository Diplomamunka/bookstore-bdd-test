package com.szelestamas.bookstorebddtest.api.category;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.ApiServiceProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CategoryApiManagementClient extends ApiManagementClient {
    public CategoryApiManagementClient(RestTemplate restTemplate, ApiServiceProperties apiServiceProperties) {
        super(restTemplate, apiServiceProperties);
    }

    public ResponseEntity<List<CategoryResource>> getCategories() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<CategoryResource> getCategory(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id), HttpMethod.GET, null, CategoryResource.class);
    }

    public ResponseEntity<CategoryResource> createCategory(CategoryDto category) {
        try {
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.PUT, new HttpEntity<>(category), CategoryResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Void> deleteCategory(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id), HttpMethod.DELETE, null, Void.class);
    }
}
