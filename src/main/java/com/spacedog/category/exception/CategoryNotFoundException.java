package com.spacedog.category.exception;

public class CategoryNotFoundException extends IllegalArgumentException {


    public CategoryNotFoundException(String message) {
        super(message);
    }
}
