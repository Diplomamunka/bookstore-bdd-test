package com.szelestamas.bookstorebddtest.api.auth;

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
public class AuthApiManagementClient extends ApiManagementClient {

    AuthApiManagementClient(RestClient restClient, String baseUrl,
                            AuthorizationProvider authProvider) {
        super(baseUrl, restClient, authProvider);
    }

    public ResponseEntity<UserResource> signUp(UserDto user) {
        return restClient.post().uri("/auth/signup").body(user).retrieve().toEntity(UserResource.class);
    }

    public ResponseEntity<UserResource> addUser(UserDto user, String personaName) {
        return restClient.post().uri("/users")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                .body(user).retrieve().toEntity(UserResource.class);
    }

    public ResponseEntity<List<UserResource>> getUsers(String personaName) {
        var baseRequest = restClient.get().uri("/users");
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(new ParameterizedTypeReference<>() {});
            return baseRequest
                    .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                    .retrieve().toEntity(new ParameterizedTypeReference<>() {});
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<UserResource> updateUser(UserDto user, String personaName) {
        var baseRequest = restClient.put().uri("/users/{login}", user.login()).body(user);
        try {
            if (personaName.isEmpty())
                return baseRequest.retrieve().toEntity(UserResource.class);
            else
                return baseRequest
                        .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                        .retrieve().toEntity(UserResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<UserResource> getProfile(String personaName) {
        try {
            return restClient.get().uri("/profile")
                    .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                    .retrieve().toEntity(UserResource.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<UserResource> updateProfile(UserDto user, String personaName) {
        return restClient.put().uri("/profile/update")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                .body(user).retrieve().toEntity(UserResource.class);
    }

    public ResponseEntity<Void> deleteUser(String login, String personaName) {
        var baseRequest = restClient.delete().uri("/users/{login}", login);
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

    public ResponseEntity<List<BookResource>> getAllBookmarks(String personaName) {
        var baseRequest = restClient.get().uri("/profile/bookmarks");
        if (personaName.isEmpty()) {
            try {
                return baseRequest.retrieve().toEntity(new ParameterizedTypeReference<>() {});
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).build();
            }
        }
        else
            return baseRequest
                    .headers(httpHeaders -> httpHeaders.setBasicAuth(authorizationProvider.authorizationForPersona(personaName)))
                    .retrieve().toEntity(new ParameterizedTypeReference<>() {});
    }
}
