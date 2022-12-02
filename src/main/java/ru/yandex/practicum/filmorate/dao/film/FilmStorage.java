package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);
    void deleteFilm(int id);
    Film updateFilm(Film film);
    Film getFilmById(int id);
    List<Film> getAllFilms();
    List<Film> getMostLikedFilms(int limit);
    List<Film> getPopularByGenreAndYear(Genre genre, int year,int count);
}
