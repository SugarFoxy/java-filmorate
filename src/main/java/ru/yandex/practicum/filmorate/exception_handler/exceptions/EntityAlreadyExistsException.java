package ru.yandex.practicum.filmorate.exception_handler.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(final String message) {
        super(message);
    }
}
