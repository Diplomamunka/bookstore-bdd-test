package com.szelestamas.bookstorebddtest.api;

import com.szelestamas.bookstorebddtest.api.author.AuthorResource;
import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.api.category.CategoryResource;
import io.cucumber.java.mk_latn.No;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
public abstract class ApiManagementClient {
    protected final RestTemplate restTemplate;
    protected final ApiServiceProperties apiServiceProperties;

    public String getApplicationUrl() {
        return apiServiceProperties.toApplicationUrl("");
    }


    public ResponseEntity<Void> callIndexPage() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl(""), HttpMethod.GET, null, Void.class);
    }
}
