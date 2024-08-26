package com.szelestamas.bookstorebddtest.api.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthorDto {
    private String fullName;
    public static AuthorDto parse(String fullName) {
        return new AuthorDto(fullName);
    }
}
