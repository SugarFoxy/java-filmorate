package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.impl.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM FILM AS F JOIN MPA AS M ON F.MPA_ID = M.ID";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Film addFilm(Film film) {
        int id = 0;
        try {
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String query = "INSERT INTO film (NAME, DESCRIPTION,RELEASE_DATE,DURATION,MPA_ID) VALUES (?, ?, ?, ?, ? );";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, Date.valueOf(film.getReleaseDate()));
                statement.setLong(4, film.getDuration());
                statement.setInt(5, film.getMpa().getId());
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getInt(1);
                    film.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return film;
    }

    @Override
    public Film updateFilms(Film film) {
        int amountLines = jdbcTemplate.update("UPDATE film SET NAME=?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASE_DATE = ?, " +
                        "DURATION= ?, " +
                        "MPA_ID =? " +
                        "WHERE ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (amountLines == 0) {
            throw new AbsenceOfObjectException("Фильм для изменения не найден");
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        Film film = new Film();
        try {
            film = jdbcTemplate.queryForObject("select * from FILM as F JOIN MPA AS M ON F.MPA_ID = M.ID where F.ID = ?", new FilmMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new AbsenceOfObjectException("Пользователь не существует");
        }
        return film;
    }
}
