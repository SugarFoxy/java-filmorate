package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();
    Genre getById(Integer id);
    List<Genre> getByFilmId(Integer filmId);
    void assignGenre(Integer filmId,Integer genreId);
    void delete(Integer filmId);
}
