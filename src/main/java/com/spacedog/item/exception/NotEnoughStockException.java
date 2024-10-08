package com.spacedog.item.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(String message) {
        super(message);
    }

    public static class ItemDuplicate extends NotEnoughStockException {
        public ItemDuplicate(String message) {
            super(message);
        }
    }

    public static class CategoryDuplicate extends NotEnoughStockException {
        public CategoryDuplicate(String message) {
            super(message);
        }
    }


}
