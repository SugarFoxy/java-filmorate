package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate REFERENCE_POINT_RELEASE_DATE = LocalDate.of(1895,12,28);
    private int id = 0;

    public int createId() {
        return id++;
    }

    @GetMapping("/films")
    public Map<Integer, Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return films;
    }

    @PostMapping("/film")
    public Film postFilm(@RequestBody Film film) {
        try {
            validation(film);
            film.setId(createId());
            films.put(film.getId(), film);
            log.info("Фильм добавлен");
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@RequestBody Film film) {
        try {
            validation(film);
            films.replace(film.getId(), film);
            log.info("Фильм онавлен");
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        return film;
    }

    private void validation(Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isAfter(REFERENCE_POINT_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}

