package com.spacedog.review.exception;

public class ReviewException extends RuntimeException {

    public ReviewException() {
        super();
    }

    public ReviewException(String message) {
        super(message);
    }

    public ReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewException(Throwable cause) {
        super(cause);
    }

    protected ReviewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
