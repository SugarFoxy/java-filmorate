package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    InMemoryFilmStorage inMemoryFilmStorage;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        try {
            inMemoryFilmStorage.postFilm(film);
            log.info("Фильм добавлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            inMemoryFilmStorage.updateFilm(film);
            log.info("Фильм обнавлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return film;
    }
}

