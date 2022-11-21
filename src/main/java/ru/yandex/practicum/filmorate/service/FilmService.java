package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage storage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage,
                       @Qualifier("likeDbStorage") LikeStorage likeStorage) {
        this.storage = storage;
        this.likeStorage = likeStorage;
    }

    public List<Film> getAllFilms() {
        log.info("Получен запрос на список всех фильмов");
        return storage.getFilms();
    }

    public Film addFilm(Film film) {
        log.info("Получен запрос на добавление фильма");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не может быть выпущен раньше 28.12.1895");
        }
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Получен запрос на обнавление фильма");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не может быть выпущен раньше 28.12.1895");
        }
        return storage.updateFilms(film);
    }

    public Film getFilmById(Integer id) {
        log.info("Получен запрос на получение фильма по ID");
        return storage.getFilmById(id);
    }

    public void like(Integer id, Integer userId) {
        log.info("Получен запрос добавления лайка");
        likeStorage.addLike(userId, id);
    }

    public void deleteLike(Integer id, Integer userId) {
        log.info("Получен запрос на удаление лайка");
        likeStorage.deleteLike(userId, id);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Получен запрос на список популярных фильмов");
        return storage.getFilms().stream()
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
