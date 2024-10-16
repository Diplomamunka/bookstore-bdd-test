package com.szelestamas.bookstorebddtest.api.auth;

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

@Component
public class AuthApiManagementClient extends ApiManagementClient {
    AuthApiManagementClient(ApiServiceProperties apiServiceProperties, RestTemplate restTemplate,
                            AuthorizationProvider authProvider) {
        super(restTemplate, apiServiceProperties, authProvider);
    }

    public ResponseEntity<UserResource> signUp(UserDto user) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/auth/signup"), HttpMethod.POST, new HttpEntity<>(user), UserResource.class);
    }

    public ResponseEntity<UserResource> addUser(UserDto user, String personaName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users"), HttpMethod.POST, new HttpEntity<>(user, headers), UserResource.class);
    }

    public ResponseEntity<List<UserResource>> getUsers(String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users"), HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<UserResource> updateUser(UserDto user, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users/" + user.login()), HttpMethod.PUT, new HttpEntity<>(user), UserResource.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users/" + user.login()), HttpMethod.PUT, new HttpEntity<>(user, headers), UserResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<UserResource> getProfile(String personaName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
        try {
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/profile"), HttpMethod.GET, new HttpEntity<>(headers), UserResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<UserResource> updateProfile(UserDto user, String personaName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/profile/update"), HttpMethod.PUT, new HttpEntity<>(user, headers), UserResource.class);
    }

    public ResponseEntity<Void> deleteUser(String login, String personaName) {
        try {
            if (personaName.isEmpty())
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users/" + login), HttpMethod.DELETE, null, Void.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/users/" + login), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<List<BookResource>> getAllBookmarks(String personaName) {
        if (personaName.isEmpty()) {
            try {
                return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/profile/bookmarks"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).build();
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authorizationProvider.authorizationForPersona(personaName));
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/profile/bookmarks"), HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
    }
}
