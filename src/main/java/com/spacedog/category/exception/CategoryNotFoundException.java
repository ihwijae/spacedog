package com.spacedog.category.exception;

public class CategoryNotFoundException extends IllegalArgumentException {


    public CategoryNotFoundException(String message) {
        super(message);
    }

    public static class CategoryOfNot extends CategoryNotFoundException {
        public CategoryOfNot(String message) {
            super(message);
        }
    }
}
