package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
public class LikesStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LikesStorage likesStorage;

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private MpaStorage mpaStorage;

    private Film film;
    private Mpa gMpa;

    @BeforeEach
    public void beforeEach() {
        gMpa = mpaStorage.getMpaById(1);
        film = new Film("Название", "Описание",
                LocalDate.of(2000, 1, 1), 30L, gMpa);
        String sqlQuery = "INSERT INTO USERS_MODEL(email, login, name, birthday) " +
                "VALUES ('e5k4p3@gmail.com', 'e5k4p3', 'e5k4p3', DATE '1995-07-11')";
        jdbcTemplate.update(sqlQuery);
    }

    @Test
    @Sql("classpath:data.sql")
    public void addLikeToFilm() {
        int id = filmStorage.addFilm(film).getId();
        likesStorage.addLikeToFilm(id, 1);
        String sqlQuery = "SELECT COUNT(like_id) AS amount FROM films_likes WHERE film_id = ?";
        SqlRowSet likeRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        likeRow.next();
        assertEquals(1, likeRow.getInt("amount"));
        assertThrows(EntityNotFoundException.class, () -> likesStorage.addLikeToFilm(9999, 1));
        assertThrows(EntityNotFoundException.class, () -> likesStorage.addLikeToFilm(1, 9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void removeLikeFromFilm() {
        int id = filmStorage.addFilm(film).getId();
        likesStorage.addLikeToFilm(id, 1);
        String sqlQuery = "SELECT COUNT(like_id) AS amount FROM films_likes WHERE film_id = ?";
        SqlRowSet likeRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        likeRow.next();
        assertEquals(1, likeRow.getInt("amount"));
        likesStorage.removeLikeFromFilm(id, 1);
        SqlRowSet newLikeRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        newLikeRow.next();
        assertEquals(0, newLikeRow.getInt("amount"));
        assertThrows(EntityNotFoundException.class, () -> likesStorage.removeLikeFromFilm(9999, 1));
        assertThrows(EntityNotFoundException.class, () -> likesStorage.removeLikeFromFilm(1, 9999));
    }
}
