package ru.yandex.practicum.filmorate.storage.genre.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Genre> getAll() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getById(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE where GENRE_ID = ?", id);
        if(genreRows.next()) {
            return new Genre(genreRows.getInt("genre_id"), genreRows.getString("genre"));
        }else {
            throw new AbsenceOfObjectException("такого жанра нет");
        }
    }

    @Override
    public List<Genre> getByFilmId(Integer filmId) {
        String sql = String.format("select * from FILM_GENRE WHERE FILM_ID = %d", filmId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public void assignGenre(Integer filmId, Integer genreId) {
        jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID,GENRE_ID) values ( ?,? )",filmId,genreId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return getById(rs.getInt("genre_id"));
    }
}
