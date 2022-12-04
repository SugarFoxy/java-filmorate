package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    } //тут

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getMostLikedFilms(int limit) {
        return filmStorage.getMostLikedFilms(limit);
    }

    public List<Film> getFilmsByDirector(int directorId, String sortBy) {
        switch(sortBy) {
            case "year":
                return filmStorage.getAllFilmsByDirectorByYear(directorId);
            case "likes":
                return filmStorage.getAllFilmsByDirector(directorId);
            default:
                throw new IllegalArgumentException("Необрабатываемый параметр sortBy - " + sortBy + ".");
        }
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> searchFilm(String query, String by) {
        return filmStorage.searchFilm(query, by);
    }

    public void addLikeToFilm(int filmId, int userId) {
        likesStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        likesStorage.removeLikeFromFilm(filmId, userId);
    }

    public void logValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                throw new ValidationException(error.getDefaultMessage());
            }
        }
    }
}
