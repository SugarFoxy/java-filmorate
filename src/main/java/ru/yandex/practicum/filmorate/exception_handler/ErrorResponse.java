package ru.yandex.practicum.filmorate.exception_handler;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}
