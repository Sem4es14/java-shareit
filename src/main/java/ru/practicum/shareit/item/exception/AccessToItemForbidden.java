package ru.practicum.shareit.item.exception;

public class AccessToItemForbidden extends RuntimeException {
    public AccessToItemForbidden(String message) {
        super(message);
    }
}
