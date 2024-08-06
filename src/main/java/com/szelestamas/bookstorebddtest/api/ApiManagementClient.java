package com.szelestamas.bookstorebddtest.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ApiManagementClient {
    private final RestTemplate restTemplate;
    private final ApiServiceProperties apiServiceProperties;


    public ResponseEntity<Void> callIndexPage() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl(""), HttpMethod.GET, null, Void.class);
    }

    public ResponseEntity<List<Category>> getCategories() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {});
    }

    public ResponseEntity<Category> getCategory(String id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + id), HttpMethod.GET, null, Category.class);
    }

    public ResponseEntity<Category> createCategory(Category category) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories"), HttpMethod.PUT, new HttpEntity<>(category), Category.class);
    }

    public ResponseEntity<Void> deleteCategory(String name) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/categories/" + name), HttpMethod.DELETE, null, Void.class);
    }
}
