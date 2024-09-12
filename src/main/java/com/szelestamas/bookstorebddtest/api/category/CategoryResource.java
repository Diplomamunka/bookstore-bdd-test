package com.szelestamas.bookstorebddtest.api.category;

public record CategoryResource(long id, String name) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryResource that = (CategoryResource) o;
        return id == that.id && name.equals(that.name);
    }
}
