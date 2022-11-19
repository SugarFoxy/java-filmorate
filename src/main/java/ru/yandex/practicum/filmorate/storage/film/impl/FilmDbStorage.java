package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("genreDbStorage")GenreStorage genreStorage,
                         @Qualifier("likeDbStorage")LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }


    @Override
    public List<Film> getFilms() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO film (NAME, DESCRIPTION,RELEASE_DATE,DURATION,RATING) " +
                        "VALUES (?, ?, ?, ?, ? );",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        return film;
    }

    @Override
    public Film updateFilms(Film film) {
        int amountLines = jdbcTemplate.update("UPDATE film SET NAME=?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASE_DATE = ?, " +
                        "DURATION= ?, " +
                        "RATING =? " +
                        "WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        film.setGenres(genreStorage.getByFilmId(film.getId()));
        film.setLikes(likeStorage.getFilmLikeId(film.getId()));
       if(amountLines == 0) {
            throw new AbsenceOfObjectException("Фильм для изменения не найден");
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where FILM_ID = ?", id);
        Film film = Film.builder()
                .id(id)
                .name(filmRows.getString("name"))
                .description(filmRows.getString("description"))
                .releaseDate(filmRows.getDate("release_date").toLocalDate())
                .duration(filmRows.getInt("duration"))
                .mpa(new MPA(filmRows.getInt("rating")))
                .build();
        film.setGenres(genreStorage.getByFilmId(film.getId()));
        film.setLikes(likeStorage.getFilmLikeId(film.getId()));
        return film;
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new MPA(rs.getInt("rating")))
                .build();
        film.setGenres(genreStorage.getByFilmId(film.getId()));
        film.setLikes(likeStorage.getFilmLikeId(film.getId()));
        return film;
    }
}
