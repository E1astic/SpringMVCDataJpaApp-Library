package ru.fil.library.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Book {

    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 40, message = "Length of name should be between 2 and 40 characters")
    private String name;

    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 40, message = "Length of author should be between 2 and 40 characters")
    private String author;

    @Max(value = 2025, message = "Year should be less then 2025")
    private int year;

    public Book(){

    }

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
