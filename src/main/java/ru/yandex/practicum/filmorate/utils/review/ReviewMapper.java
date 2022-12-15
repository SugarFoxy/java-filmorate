package ru.yandex.practicum.filmorate.utils.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.model.User;

@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public FilmReview mapReview(SqlRowSet sqlRowSet) {
        User user = userStorage.getUserById(sqlRowSet.getInt("user_id"));
        Film film = filmStorage.getFilmById(sqlRowSet.getInt("film_id"));
        return new FilmReview(sqlRowSet.getInt("review_id"),
                sqlRowSet.getString("content"),
                sqlRowSet.getBoolean("is_positive"),
                sqlRowSet.getInt("useful"), user, film);
    }
}
