package ru.yandex.practicum.filmorate.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controllers.*;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.ValidationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {FilmController.class,
        UserController.class,
        MpaController.class,
        GenreController.class,
        ReviewController.class,
        DirectorController.class})

public class ExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Ошибка валидации.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Объект не был найден.", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExists(final EntityAlreadyExistsException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Пользователь с таким логином или email уже существует.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(final IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Необрабатываемый параметр запроса.", e.getMessage());
    }
}
