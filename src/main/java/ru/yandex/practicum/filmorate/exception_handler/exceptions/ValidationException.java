package ru.yandex.practicum.filmorate.exception_handler.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
