package com.spacedog.exception;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException(String message) {
        super(message);
    }



    public static class RefreshTokenNullException extends RefreshTokenException {
        public RefreshTokenNullException(String message) {
            super(message);
        }
    }
        public static class RefreshTokenExpiredException extends RefreshTokenException {
            public RefreshTokenExpiredException(String message) {
                super(message);
            }
        }

        public static class RefreshTokenInvalidException extends RefreshTokenException {
            public RefreshTokenInvalidException(String message) {
                super(message);
            }
        }

        public static class RefreshTokenDataBase extends RefreshTokenException {
            public RefreshTokenDataBase(String message) {
                super(message);
            }
        }

    }
