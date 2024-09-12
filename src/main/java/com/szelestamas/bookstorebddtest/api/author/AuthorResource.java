package com.szelestamas.bookstorebddtest.api.author;

public record AuthorResource(long id, String fullName) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorResource that = (AuthorResource) o;
        return id == that.id && fullName.equals(that.fullName);
    }
}
