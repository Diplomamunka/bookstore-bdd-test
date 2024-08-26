package com.szelestamas.bookstorebddtest.api;

import io.cucumber.java.DataTableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    private String title;
    private int price;
    private Category category;
    private String shortDescription;
    private int discount;
    private boolean available;
    private LocalDate releaseDate;
    private String icon;

    @DataTableType
    public Book bookEntry(Map<String, String> entry) {
        return new Book(

        );
    }
}
