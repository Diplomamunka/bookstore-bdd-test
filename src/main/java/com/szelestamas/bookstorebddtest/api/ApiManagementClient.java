package com.szelestamas.bookstorebddtest.api;

import com.szelestamas.bookstorebddtest.core.authorization.AuthorizationProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public abstract class ApiManagementClient {
    @Getter
    private final String baseUrl;
    protected final RestClient restClient;
    protected final AuthorizationProvider authorizationProvider;
}
