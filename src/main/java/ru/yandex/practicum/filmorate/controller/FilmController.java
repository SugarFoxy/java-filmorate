package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return filmService.findAll();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        try {
            filmService.postFilm(film);
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
            filmService.updateFilm(film);
            log.info("Фильм обнавлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return film;
    }
}

