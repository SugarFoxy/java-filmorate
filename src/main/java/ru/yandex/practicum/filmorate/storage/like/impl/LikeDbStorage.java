package ru.yandex.practicum.filmorate.storage.like.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int userId, int filmId) {
        jdbcTemplate.update("INSERT INTO LIKE_USER_FILM (film_id, user_id) VALUES (?, ?);", filmId, userId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        int amountLines = jdbcTemplate.update("DELETE FROM LIKE_USER_FILM WHERE film_id = ? AND user_id = ?;", filmId, userId);
        if(amountLines == 0) {
            throw new AbsenceOfObjectException(String.format("Пользователь %d не ставил лай для фильма %d",userId, filmId));
        }
    }

    public int getFilmLikeId(long filmId) {
        String sqlQuery = "SELECT user_id FROM LIKE_USER_FILM WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::createLikeId, filmId).size();
    }

    private long createLikeId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("user_id");
    }
}
