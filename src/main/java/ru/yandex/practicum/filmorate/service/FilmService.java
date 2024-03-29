package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.controllers.sorts.SearchBy;
import ru.yandex.practicum.filmorate.controllers.sorts.SortBy;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public FilmService(
            FilmStorage filmStorage, LikesStorage likesStorage, GenreStorage genreStorage,
            ApplicationEventPublisher publisher) {
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
        this.publisher = publisher;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getMostLikedFilms(Integer genre, Integer year, int count) {
        if (genre == null && year == null) {
            return filmStorage.getMostLikedFilms(count);
        } else if (genre == null) {
            return filmStorage.getPopularByGenreAndYear(null, year, count);
        } else {
            return filmStorage.getPopularByGenreAndYear(genreStorage.getGenreById(genre), year, count);
        }
    }

    public List<Film> getFilmsByDirector(int directorId, SortBy sortBy) {
        switch (sortBy) {
            case YEAR:
                return filmStorage.getAllFilmsByDirectorByYear(directorId);
            case LIKES:
                return filmStorage.getAllFilmsByDirector(directorId);
            default:
                throw new IllegalArgumentException("Необрабатываемый параметр sortBy - " + sortBy + ".");
        }
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> searchFilm(String query, List<SearchBy> by) {
        return filmStorage.searchFilm(query, by);
    }

    public void addLikeToFilm(int filmId, int userId) {
        likesStorage.addLikeToFilm(filmId, userId);

        publisher.publishEvent(new FeedEvent(userId, EventType.LIKE, Operation.ADD, filmId));
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        likesStorage.removeLikeFromFilm(filmId, userId);

        publisher.publishEvent(new FeedEvent(userId, EventType.LIKE, Operation.REMOVE, filmId));
    }

    public void logValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                throw new ValidationException(error.getDefaultMessage());
            }
        }
    }
}
