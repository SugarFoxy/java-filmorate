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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.director.DirectorUtils;
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
    private final DirectorUtils directorUtils;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmUtils filmUtils, DirectorUtils directorUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmUtils = filmUtils;
        this.directorUtils = directorUtils;
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
        addFilmDirectors(film);
        log.info("Фильм с названием " + film.getName() + " добавлен.");
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        if (filmUtils.getSqlRowSetByFilmId(id).next()) {
            String filmSqlQuery = "DELETE FROM films_model WHERE film_id = ?";
            jdbcTemplate.update(filmSqlQuery, id);
            removeFilmGenres(id);
            removeFilmDirectors(id);
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
            removeFilmDirectors(film.getId());
            addFilmDirectors(film);
            film.setGenres(filmUtils.getFilmGenres(film.getId()));
            film.setDirectors(filmUtils.getFilmDirectors(film.getId()));
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
            return FilmMapper.mapFilm(filmRow,
                    filmUtils.getFilmMpa(mpaId),
                    filmUtils.getFilmGenres(filmId),
                    filmUtils.getFilmDirectors(filmId));
        } else {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден.");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films_model";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);
        return fillListWithFilms(filmRows);
    }

    public List<Film> getMostLikedFilms(int limit) {
        String sqlQuery = "SELECT fm.*, COUNT(fl.like_id) FROM films_model AS fm " +
                "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                "GROUP BY fm.film_id ORDER BY COUNT(fl.like_id) DESC LIMIT ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, limit);
        return fillListWithFilms(filmRows);
    }

    public List<Film> getAllFilmsByDirector(int directorId) {
        if (!directorUtils.getSqlRowSetByDirectorId(directorId).next()) {
            throw new EntityNotFoundException("Режиссер с id " + directorId + " не найден.");
        }
        String sqlQuery = "SELECT fm.*, COUNT(fl.like_id) FROM films_model AS fm " +
                "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                "RIGHT OUTER JOIN films_directors AS fd on fm.film_id = fd.film_id " +
                "WHERE fd.director_id = ?" +
                "GROUP BY fm.film_id ORDER BY COUNT(fl.like_id) DESC";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, directorId);
        return fillListWithFilms(filmRows);
    }

    public List<Film> getAllFilmsByDirectorByYear(int directorId) {
        if (!directorUtils.getSqlRowSetByDirectorId(directorId).next()) {
            throw new EntityNotFoundException("Режиссер с id " + directorId + " не найден.");
        }
        String sqlQuery = "SELECT fm.* FROM films_model AS fm " +
                "RIGHT OUTER JOIN films_directors AS fd on fm.film_id = fd.film_id " +
                "WHERE fd.director_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, directorId);
        return fillListWithFilms(filmRows).stream()
                .sorted(Comparator.comparing(Film::getReleaseDate))
                .collect(Collectors.toList());
    }

    private void addFilmDirectors(Film film) {
        checkDirectorExistence(film);
        String sqlQuery = "INSERT INTO films_directors(film_id, director_id) VALUES (?, ?)";
        for (Director director : film.getDirectors()) {
            jdbcTemplate.update(sqlQuery, film.getId(), director.getId());
        }
    }

    private void removeFilmDirectors(int id) {
        String sqlQuery = "DELETE FROM films_directors WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private void checkDirectorExistence(Film film) {
        for (Director director : film.getDirectors()) {
            if (!directorUtils.getSqlRowSetByDirectorId(director.getId()).next()) {
                throw new EntityNotFoundException("Режиссер с id " + director.getId() + " не найден.");
            }
        }
    }

    private void addFilmGenres(Film film) {
        checkGenreExistence(film);
        String sqlQuery = "INSERT INTO films_genres(film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    private void removeFilmGenres(int id) {
        String sqlQuery = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private List<Film> fillListWithFilms(SqlRowSet filmRows) {
        List<Film> allFilms = new ArrayList<>();
        while (filmRows.next()) {
            int filmId = filmUtils.getFilmId(filmRows);
            int mpaId = filmUtils.getFilmMpaId(filmRows);
            allFilms.add(FilmMapper.mapFilm(filmRows,
                    filmUtils.getFilmMpa(mpaId),
                    filmUtils.getFilmGenres(filmId),
                    filmUtils.getFilmDirectors(filmId)));
        }
        return allFilms;
    }

    private void checkGenreExistence(Film film) {
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
            commonFilms.add(FilmMapper.mapFilm(filmRows,
                    filmUtils.getFilmMpa(mpaId),
                    filmUtils.getFilmGenres(filmId),
                    filmUtils.getFilmDirectors(filmId)));
        }
        return commonFilms;
    }

    public List<Film> searchFilm(String query, String searchBy) {
        final String stmtForFilm =
                "SELECT * FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                        "WHERE LOWER(fm.title) LIKE LOWER('%'||?||'%') " +
                        "GROUP BY fm.film_id ORDER BY COUNT(fl.like_id) DESC";
        final String stmtForDirector =
                "SELECT * FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                        "WHERE fm.film_id in " +
                        "(SELECT fd.film_id FROM films_directors fd " +
                        "LEFT JOIN directors_model dm ON fd.director_id = dm.DIRECTOR_ID " +
                        "WHERE LOWER(dm.director_name) LIKE LOWER('%'||?||'%'))" +
                        "GROUP BY fm.film_id ORDER BY COUNT(fl.like_id) DESC";
        final String stmtForDirectorAndTitle =
                "SELECT fm.*, COUNT(fl.LIKE_ID) as likes FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                        "WHERE lower(fm.title) LIKE LOWER('%'||?||'%') " +
                        "group by fm.FILM_ID " +
                        "UNION " +
                        "SELECT fm.*, COUNT(fl.LIKE_ID) as likes FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl on fm.film_id = fl.film_id " +
                        "WHERE fm.film_id in " +
                        "(SELECT fd.film_id FROM films_directors fd " +
                        "LEFT JOIN directors_model dm ON fd.director_id = dm.DIRECTOR_ID " +
                        "WHERE LOWER(dm.director_name) LIKE LOWER('%'||?||'%')) " +
                        "group by fm.FILM_ID " +
                        "order by likes desc";
        SqlRowSet filmRows;
        switch (searchBy) {
            case "title":
                filmRows = jdbcTemplate.queryForRowSet(stmtForFilm, query);
                break;
            case "director":
                filmRows = jdbcTemplate.queryForRowSet(stmtForDirector, query);
                break;
            case "director,title":
            case "title,director":
                filmRows = jdbcTemplate.queryForRowSet(stmtForDirectorAndTitle, query, query);
                break;
            default:
                throw new IllegalStateException("Unexpected value *searchBy* : " + searchBy);
        }
        return fillListWithFilms(filmRows);
    }

}
