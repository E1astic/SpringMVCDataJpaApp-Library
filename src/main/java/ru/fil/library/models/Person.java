package ru.fil.library.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class Person {

    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min=2, max=40, message = "Length of name should be between 2 and 40 characters")
    private String name;

    @Min(value=1900, message = "Year of birth should be between 1900 and 2025")
    @Max(value=2025, message = "Year of birth should be between 1900 and 2025")
    private int year;

    public Person(){

    }

    public Person(int id, String name, int year) {
        this.id = id;
        this.name = name;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
