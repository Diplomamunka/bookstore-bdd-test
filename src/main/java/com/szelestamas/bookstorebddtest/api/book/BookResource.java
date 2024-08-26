package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.author.AuthorResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResource {
    private long id;
    private String title;
    private int price;
    private String category;
    private String shortDescription;
    private int discount;
    private List<AuthorResource> authors;
    private boolean available;
    private LocalDate releaseDate;
    private String icon;
}
