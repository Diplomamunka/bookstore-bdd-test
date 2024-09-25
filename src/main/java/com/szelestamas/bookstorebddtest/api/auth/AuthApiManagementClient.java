package com.szelestamas.bookstorebddtest.api.auth;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.ApiServiceProperties;
import com.szelestamas.bookstorebddtest.core.authorization.AuthorizationProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthApiManagementClient extends ApiManagementClient {
    private final AuthorizationProvider authProvider;
    AuthApiManagementClient(ApiServiceProperties apiServiceProperties, RestTemplate restTemplate,
                            AuthorizationProvider authProvider) {
        super(restTemplate, apiServiceProperties);
        this.authProvider = authProvider;
    }

    public void signIn(String personaName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(authProvider.authorizationForPersona(personaName));
        restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/3"), HttpMethod.DELETE, new HttpEntity<>(httpHeaders), Void.class);
    }
}
