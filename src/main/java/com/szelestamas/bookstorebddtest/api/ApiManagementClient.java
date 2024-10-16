package com.szelestamas.bookstorebddtest.api;

import com.szelestamas.bookstorebddtest.core.authorization.AuthorizationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class ApiManagementClient {
    protected final RestTemplate restTemplate;
    protected final ApiServiceProperties apiServiceProperties;
    protected final AuthorizationProvider authorizationProvider;

    public String getApplicationUrl() {
        return apiServiceProperties.toApplicationUrl("");
    }


    public ResponseEntity<Void> callIndexPage() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl(""), HttpMethod.GET, null, Void.class);
    }
}
