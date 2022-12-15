package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.controllers.sorts.QueryBy;
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

    List<Film> getPopularByGenreAndYear(Genre genre, Integer year, int count);

    List<Film> getAllFilmsByDirector(int directorId);

    List<Film> getAllFilmsByDirectorByYear(int directorId);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> searchFilm(String query, List<QueryBy> by);

    List<Film> getRecommendationsByUser(int userId);
}
