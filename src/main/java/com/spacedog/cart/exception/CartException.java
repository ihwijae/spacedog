package com.spacedog.cart.exception;

public class CartException extends RuntimeException {

    public CartException(String message) {
        super(message);
    }

    public static class NotFoundCartException extends CartException{

        public NotFoundCartException(String message) {
            super(message);
        }
    }



}
