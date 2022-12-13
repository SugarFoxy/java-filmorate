package ru.yandex.practicum.filmorate.dao.likes;

public interface LikesStorage {
    void addLikeToFilm(int filmId, int userId);
    void removeLikeFromFilm(int filmId, int userId);
}
