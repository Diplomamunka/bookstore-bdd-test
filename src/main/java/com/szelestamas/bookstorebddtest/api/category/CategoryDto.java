package com.szelestamas.bookstorebddtest.api.category;

public record CategoryDto(String name) {
    public static CategoryDto parse(String name) {
        return new CategoryDto(name);
    }
}
