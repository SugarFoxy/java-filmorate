package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return service.getAllFilms();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Фильм добавлен");
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
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

