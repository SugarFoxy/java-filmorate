package ru.yandex.practicum.filmorate.dao.likes.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.utils.film.FilmUtils;
import ru.yandex.practicum.filmorate.utils.user.UserUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmUtils filmUtils;
    private final UserUtils userUtils;

    @Autowired
    public LikesDbStorage(JdbcTemplate jdbcTemplate, FilmUtils filmUtils, UserUtils userUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmUtils = filmUtils;
        this.userUtils = userUtils;
    }

    @Override
    public int addLikeToFilm(int filmId, int userId) {
        checkFilmId(filmId);
        checkUserId(userId);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("like_id")
                .withTableName("films_likes");
        int likeId = jdbcInsert
                .executeAndReturnKey(Map.of("film_id", filmId, "user_id", userId))
                .intValue();

        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId + ".");

        return likeId;
    }

    @Override
    public int removeLikeFromFilm(int filmId, int userId) {
        checkFilmId(filmId);
        checkUserId(userId);

        int likeId = 0;
        List<Integer> ids = jdbcTemplate.query(
                "SELECT like_id FROM films_likes WHERE film_id = ? AND user_id = ?",
                (rs, rowNum) -> rs.getInt("like_id"),
                filmId, userId);
        if (ids.size() > 0) {
            likeId = ids.get(0);
            String sqlQuery = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        }

        log.info("Пользователь с id " + userId + " убрал лайк с фильма с id " + filmId + ".");

        return likeId;
    }

    private void checkFilmId(int filmId) {
        SqlRowSet filmRow = filmUtils.getSqlRowSetByFilmId(filmId);
        if (!filmRow.next()) {
            throw new EntityNotFoundException("Фильм с id " + filmId + " не найден.");
        }
    }

    private void checkUserId(int userId) {
        SqlRowSet userRow = userUtils.getSqlRowSetByUserId(userId);
        if (!userRow.next()) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не найден.");
        }
    }
}
