package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    List<Film> getFilms();
    Film addFilm(Film film);
    void updateFilms(Film film);
    Film getFilmById(Integer id );
    public Map<Integer,Film> getMapFilms();
}
