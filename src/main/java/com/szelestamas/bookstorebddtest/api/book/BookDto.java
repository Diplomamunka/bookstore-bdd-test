package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.author.AuthorDto;
import io.cucumber.java.DataTableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String title;
    private int price;
    private String category;
    private String shortDescription;
    private int discount;
    private List<AuthorDto> authors;
    private boolean available;
    private LocalDate releaseDate;
    private String icon;

    @DataTableType
    public static BookDto bookEntry(Map<String, String> entry) {
        if (entry.size() == 6)
            return new BookDto(
                    entry.get("title"),
                    Integer.parseInt(entry.get("price")),
                    entry.get("category"),
                    "",
                    Integer.parseInt(entry.get("discount")),
                    Arrays.stream(entry.get("authors").split(", ")).map(AuthorDto::parse).toList(),
                    Boolean.parseBoolean(entry.get("available")),
                    null,
                    ""
            );
        else
            return new BookDto(
                    entry.get("title"),
                    Integer.parseInt(entry.get("price")),
                    entry.get("category"),
                    entry.get("short description"),
                    Integer.parseInt(entry.get("discount")),
                    Arrays.stream(entry.get("authors").split(", ")).map(AuthorDto::parse).toList(),
                    Boolean.parseBoolean(entry.get("available")),
                    LocalDate.parse(entry.get("release date")),
                    entry.get("icon")
            );
    }
}
