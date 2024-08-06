package com.szelestamas.bookstorebddtest.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiServiceProperties {
    @Value("${website-url}")
    private String url;

    public String toApplicationUrl(String path) {
        return url + path;
    }
}
