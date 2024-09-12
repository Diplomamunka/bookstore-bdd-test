package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.ApiManagementClient;
import com.szelestamas.bookstorebddtest.api.ApiServiceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.List;

@Component
public class BookApiManagementClient extends ApiManagementClient {
    @Value("${file-location}")
    String filesPath;

    BookApiManagementClient(RestTemplate restTemplate, ApiServiceProperties apiServiceProperties) {
        super(restTemplate, apiServiceProperties);
    }

    public ResponseEntity<BookResource> getBook(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id), HttpMethod.GET, null, BookResource.class);
    }

    public ResponseEntity<List<BookResource>> getBooks() {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books"), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<BookResource> updateBook(long id, BookDto book) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id), HttpMethod.PUT, new HttpEntity<>(book), BookResource.class);
    }

    public ResponseEntity<Void> deleteBook(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id), HttpMethod.DELETE, null, Void.class);
    }

    public ResponseEntity<BookResource> createBook(BookDto book) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books"), HttpMethod.PUT, new HttpEntity<>(book), BookResource.class);
    }

    public ResponseEntity<String> uploadImage(long id, Path file) {
        MultiValueMap<String, Object> files = new LinkedMultiValueMap<>();
        files.add("image", new FileSystemResource(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        try {
            return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id + "/image"), HttpMethod.POST, new HttpEntity<>(files, httpHeaders), String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public ResponseEntity<Resource> downloadImage(long id) {
        return restTemplate.exchange(apiServiceProperties.toApplicationUrl("/books/" + id + "/image"), HttpMethod.GET, null, Resource.class);
    }
}
