package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.author.AuthorResource;
import com.szelestamas.bookstorebddtest.api.category.CategoryResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResource {
    private long id;
    private String title;
    private int price;
    private CategoryResource category;
    private String shortDescription;
    private int discount;
    private List<AuthorResource> authors;
    private boolean available;
    private LocalDate releaseDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookResource that = (BookResource) o;
        return id == that.id && title.equals(that.title) && price == that.price && discount == that.discount &&
                available == that.available && ((releaseDate == that.releaseDate) || releaseDate.equals(that.releaseDate)) &&
                category.equals(that.category) && (new HashSet<>(authors).equals(new HashSet<>(that.authors)));
    }
}
