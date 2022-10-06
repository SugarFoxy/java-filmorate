package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate REFERENCE_POINT_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;

    private int createId() {
        return id++;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return getListFilms();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        try {
            validation(film);
            film.setId(createId());
            films.put(film.getId(), film);
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
            validation(film);
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
                log.info("Фильм обнавлен");
            } else {
                throw new ValidationException("Такого фильма не существует");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);

        }
        return film;
    }

    private List<Film> getListFilms() {
        Collection<Film> value = films.values();
        return new ArrayList<>(value);
    }

    private void validation(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(REFERENCE_POINT_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}

