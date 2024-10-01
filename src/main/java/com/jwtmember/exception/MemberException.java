package com.jwtmember.exception;

public class MemberException extends RuntimeException {


    public MemberException(String message) {
        super(message);
    }



    public static class EmailDuplicateException extends MemberException {
        public EmailDuplicateException(String message) {
            super(message);
        }
    }

    public static class NickNameDuplicateException extends MemberException {
        public NickNameDuplicateException(String message) {
            super(message);
        }
    }

    public static class CheckedPassWordDuplicateException extends MemberException {
        public CheckedPassWordDuplicateException(String message) {
            super(message);
        }
    }

    public static class PassWordDuplicateException extends MemberException {

        public PassWordDuplicateException(String message) {
            super(message);
        }
    }
}
