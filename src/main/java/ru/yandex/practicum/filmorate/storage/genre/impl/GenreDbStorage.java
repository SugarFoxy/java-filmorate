package ru.yandex.practicum.filmorate.storage.genre.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
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
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre getById(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE where ID = ?", id);
        if (genreRows.next()) {
            return new Genre(genreRows.getInt("id"), genreRows.getString("genre"));
        } else {
            throw new AbsenceOfObjectException("такого жанра нет");
        }
    }

    @Override
    public List<Genre> getByFilmId(Integer filmId) {
        String sql = "select fg.GENRE_ID as ID, g.genre from FILM_GENRE as fg JOIN GENRE as g ON fg.GENRE_ID = g.ID where FILM_ID = ?;";
        return jdbcTemplate.query(sql, new  GenreMapper(),filmId);
    }

    @Override
    public void assignGenre(Integer filmId, Integer genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from FILM_GENRE where FILM_ID = ? and GENRE_ID = ?;", filmId, genreId);
        if (!genreRows.next()) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID,GENRE_ID) values ( ?,? )", filmId, genreId);
        }
    }

    @Override
    public void delete(Integer filmId) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE film_id = ?;", filmId);
    }

    public static class GenreMapper implements RowMapper<Genre>{
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("genre"));
            return genre;
        }
    }
}
