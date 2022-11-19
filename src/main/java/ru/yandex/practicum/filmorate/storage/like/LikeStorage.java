package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {
    void addLike(int userId, int filmId);
    void deleteLike(int userId, int filmId);
    List<Integer> getFilmLikeId(long filmId);

}
