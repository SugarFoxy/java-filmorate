package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate REFERENCE_POINT_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;

    private int createId() {
        return id++;
    }

    public List<Film> findAll() {
        Collection<Film> value = films.values();
        return new ArrayList<>(value);
    }

    public Film postFilm(Film film) {
        try {
            validation(film);
            film.setId(createId());
            films.put(film.getId(), film);
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        try {
            validation(film);
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
            } else {
                throw new ValidationException("Такого фильма не существует");
            }
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return film;
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

