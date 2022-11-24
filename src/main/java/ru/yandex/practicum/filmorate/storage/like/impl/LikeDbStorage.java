package ru.yandex.practicum.filmorate.storage.like.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int userId, int filmId) {
        int amountLines = jdbcTemplate.update("INSERT INTO LIKE_USER_FILM (film_id, user_id) VALUES (?, ?);", filmId, userId);
        if (amountLines == 0) {
            throw new AbsenceOfObjectException(String.format("Пользователь %d уже поставил лайк фильму %d", userId, filmId));
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        int amountLines = jdbcTemplate.update("DELETE FROM LIKE_USER_FILM WHERE film_id = ? AND user_id = ?;", filmId, userId);
        if (amountLines == 0) {
            throw new AbsenceOfObjectException(String.format("Пользователь %d не ставил лайк для фильма %d", userId, filmId));
        }
    }

    @Override
    public List<Integer> getFilmLikeId(int filmId) {
        String sqlQuery = "SELECT user_id FROM LIKE_USER_FILM WHERE film_id = ?";
        try {
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createLikeId(rs), filmId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private int createLikeId(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }
}
