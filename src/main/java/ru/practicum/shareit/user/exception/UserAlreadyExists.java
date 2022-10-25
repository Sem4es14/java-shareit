package ru.practicum.shareit.user.exception;

public class UserAlreadyExists extends  RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }
}
