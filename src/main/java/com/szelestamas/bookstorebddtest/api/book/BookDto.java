package com.szelestamas.bookstorebddtest.api.book;

import com.szelestamas.bookstorebddtest.api.author.AuthorDto;
import com.szelestamas.bookstorebddtest.api.category.CategoryDto;
import io.cucumber.java.DataTableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
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
    private CategoryDto category;
    private String shortDescription;
    private int discount;
    private List<String> tags;
    private List<AuthorDto> authors;
    private boolean available;
    private LocalDate releaseDate;

    @DataTableType
    public static BookDto bookEntry(Map<String, String> entry) {
        if (entry.size() == 6)
            return new BookDto(
                    entry.get("title"),
                    Integer.parseInt(entry.get("price")),
                    CategoryDto.parse(entry.get("category")),
                    null,
                    Integer.parseInt(entry.get("discount")),
                    new ArrayList<>(),
                    Arrays.stream(entry.get("authors").split(", ")).map(AuthorDto::parse).toList(),
                    Boolean.parseBoolean(entry.get("available")),
                    null
            );
        else
            return new BookDto(
                    entry.get("title"),
                    Integer.parseInt(entry.get("price")),
                    CategoryDto.parse(entry.get("category")),
                    entry.get("short description"),
                    Integer.parseInt(entry.get("discount")),
                    Arrays.stream(entry.get("tags").split(",")).toList(),
                    Arrays.stream(entry.get("authors").split(", ")).map(AuthorDto::parse).toList(),
                    Boolean.parseBoolean(entry.get("available")),
                    LocalDate.parse(entry.get("release date"), new DateTimeFormatterBuilder()
                            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            .appendOptional(DateTimeFormatter.ofPattern("yyyy"))
                            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1).toFormatter())
            );
    }
}
