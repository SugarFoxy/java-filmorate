package ru.yandex.practicum.filmorate.dao.likes;

public interface LikesStorage {
    int addLikeToFilm(int filmId, int userId);
    int removeLikeFromFilm(int filmId, int userId);
}
