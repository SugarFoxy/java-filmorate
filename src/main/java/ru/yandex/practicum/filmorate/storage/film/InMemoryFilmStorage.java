package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    private int createId() {
        return id++;
    }


    @Override
    public List<Film> getFilms() {
        Collection<Film> films1 = films.values();
        return new ArrayList<>(films1);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(createId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilms(Film film) {
        if (films.containsKey(film.getId())) {
            film.setLikes(films.get(film.getId()).getLikes());
            films.put(film.getId(), film);
            return film;
        }else {
            throw new AbsenceOfObjectException("Фильм не существует");
        }
    }

    @Override
    public Film getFilmById(Integer id) {
        if(films.containsKey(id)){
            return films.get(id);
        }else {
            throw new AbsenceOfObjectException("Фильм не найден");
        }
    }

    @Override
    public Map<Integer, Film> getMapFilms() {
        return null;
    }
}

