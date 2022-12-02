package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.film.FilmMapper;
import ru.yandex.practicum.filmorate.utils.film.FilmUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmUtils filmUtils;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmUtils filmUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmUtils = filmUtils;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films_model(title, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        addFilmGenres(film);
        log.info("Фильм с названием " + film.getName() + " добавлен.");
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        if (filmUtils.getSqlRowSetByFilmId(id).next()) {
            String filmSqlQuery = "DELETE FROM films_model WHERE film_id = ?";
            jdbcTemplate.update(filmSqlQuery, id);
            removeFilmGenres(id);
            log.info("Фильм с id " + id + " удален.");
        } else {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден.");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmUtils.getSqlRowSetByFilmId(film.getId()).next()) {
            String sqlQuery = "UPDATE films_model SET title = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                    "WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            removeFilmGenres(film.getId());
            addFilmGenres(film);
            film.setGenres(filmUtils.getFilmGenres(film.getId()));
            log.info("Фильм с id " + film.getId() + " обновлен.");
            return film;
        } else {
            throw new EntityNotFoundException("Фильм с id " + film.getId() + " не найден.");
        }
    }

    @Override
    public Film getFilmById(int id) {
        SqlRowSet filmRow = filmUtils.getSqlRowSetByFilmId(id);
        if (filmRow.next()) {
            int filmId = filmUtils.getFilmId(filmRow);
            int mpaId = filmUtils.getFilmMpaId(filmRow);
            return FilmMapper.mapFilm(filmRow, filmUtils.getFilmMpa(mpaId), filmUtils.getFilmGenres(filmId));
        } else {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден.");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        String sqlQuery = "SELECT * FROM films_model";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);
        while (filmRows.next()) {
            int filmId = filmUtils.getFilmId(filmRows);
            int mpaId = filmUtils.getFilmMpaId(filmRows);
            allFilms.add(FilmMapper.mapFilm(filmRows, filmUtils.getFilmMpa(mpaId), filmUtils.getFilmGenres(filmId)));
        }
        return allFilms;
    }

    public List<Film> getMostLikedFilms(int limit) {
        List<Film> topFilms = new ArrayList<>();
        String sqlQuery = "SELECT fm.*, COUNT(fl.like_id) FROM films_model AS fm " +
                "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                "GROUP BY fm.film_id ORDER BY COUNT(fl.like_id) DESC LIMIT ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, limit);
        while (filmRows.next()) {
            int filmId = filmUtils.getFilmId(filmRows);
            int mpaId = filmUtils.getFilmMpaId(filmRows);
            topFilms.add(FilmMapper.mapFilm(filmRows, filmUtils.getFilmMpa(mpaId), filmUtils.getFilmGenres(filmId)));
        }
        return topFilms;
    }

    @Override
    public List<Film> getPopularByGenreAndYear(Genre genre, int year, int count) {
        TreeSet<Film> films = new TreeSet<>((f1, f2) -> filmUtils.getAmountLikesByFilmId(f1) - filmUtils.getAmountLikesByFilmId(f2));
        films.addAll(getAllFilms().stream()
                .filter(film -> film.getGenres().contains(genre))
                .filter(film -> film.getReleaseDate().getYear() == year)
                .limit(count)
                .collect(Collectors.toList()));
        return new ArrayList<>(films.descendingSet());
    }

    private void addFilmGenres(Film film) {
        checkGenreIdExistence(film);
        String sqlQuery = "INSERT INTO films_genres(film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    private void removeFilmGenres(int id) {
        String sqlQuery = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private void checkGenreIdExistence(Film film) {
        String sqlQuery = "SELECT * FROM genre_dictionary WHERE genre_id = ?";
        for (Genre genre : film.getGenres()) {
            if (!jdbcTemplate.queryForRowSet(sqlQuery, genre.getId()).next()) {
                throw new EntityNotFoundException("Жанр с id " + genre.getId() + " не найден.");
            }
        }
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        List<Film> commonFilms = new ArrayList<>();
        String sqlQuery = "SELECT * FROM films_model " +
                "WHERE film_id IN (SELECT film_id FROM films_likes WHERE user_id = ?  " +
                "INTERSECT SELECT film_id FROM films_likes WHERE user_id = ?) ";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        while (filmRows.next()) {
            int filmId = filmUtils.getFilmId(filmRows);
            int mpaId = filmUtils.getFilmMpaId(filmRows);
            commonFilms.add(FilmMapper.mapFilm(filmRows, filmUtils.getFilmMpa(mpaId), filmUtils.getFilmGenres(filmId)));
        }
        return commonFilms;
    }
}
