package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controllers.sorts.QueryBy;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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
        film.setGenres(filmUtils.getFilmGenres(film.getId()));
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

    @Override
    public List<Film> getPopularByGenreAndYear(Genre genre, int year, int count) {
        return getAllFilms().stream()
                .filter(getFilter(genre, year))
                .sorted(Comparator.comparingInt(filmUtils::getAmountLikesByFilmId).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Predicate<Film> getFilter(Genre genre, int year) {
        if (genre == null) {
            return (Film film) -> film.getReleaseDate().getYear() == year;
        } else if (year <= 0) {
            return (Film film) -> film.getGenres().contains(genre);
        } else {
            return (Film film) -> film.getGenres().contains(genre) && film.getReleaseDate().getYear() == year;
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

    public List<Film> searchFilm(String query, List<QueryBy> searchBy) {
        final String stmtForFilm =
                "SELECT fm.*, COUNT(fl.like_id) as count_like FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl ON fm.film_id = fl.film_id " +
                        // запрос вернет все данные из таблицы films_model и количество лайков для каждого фильма
                        "WHERE fm.title ILIKE '%'||?||'%' " +
                        // в названии которых содержится искомая подстрока
                        "GROUP BY fm.film_id ORDER BY count_like DESC";
        // отсортированные по количеству лайков
        final String stmtForDirector =
                "SELECT fm.*, COUNT(fl.like_id) as count_like FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl ON fm.film_id = fl.film_id " +
                        // запрос вернет все данные из таблицы films_model и количество лайков для каждого фильма
                        "WHERE fm.film_id in " +
                        "(SELECT fd.film_id FROM films_directors fd " +
                        "LEFT JOIN directors_model dm ON fd.director_id = dm.DIRECTOR_ID " +
                        "WHERE dm.director_name ILIKE '%'||?||'%')" +
                        // в имени режиссера которых содержится искомая подстрока
                        "GROUP BY fm.film_id ORDER BY count_like DESC";
        // отсортированные по количеству лайков
        final String stmtForDirectorAndTitle =
                "SELECT fm.*, COUNT(fl.like_id) as count_like FROM films_model fm " +
                        "LEFT OUTER JOIN films_likes AS fl ON fm.film_id = fl.film_id " +
                        // запрос вернет все данные из таблицы films_model и количество лайков для каждого фильма
                        "WHERE fm.title ILIKE '%'||?||'%' OR " +
                        // в названии которых содержится искомая подстрока
                        "fm.film_id in " +
                        "(SELECT fd.film_id FROM films_directors fd " +
                        "LEFT JOIN directors_model d ON fd.director_id = d.DIRECTOR_ID " +
                        "WHERE d.director_name ILIKE '%'||?||'%') " +
                        // либо в имени режиссера которых содержится та же подстрока
                        "GROUP BY fm.film_id ORDER BY count_like DESC";
        // отсортированные по количеству лайков

        SqlRowSet filmRows;
        switch (searchBy.size()) {
            case 1:
                switch (searchBy.get(0)) {
                    case TITLE:
                        filmRows = jdbcTemplate.queryForRowSet(stmtForFilm, query);
                        break;
                    case DIRECTOR:
                        filmRows = jdbcTemplate.queryForRowSet(stmtForDirector, query);
                        break;
                    default:
                        throw new IllegalArgumentException("Необрабатываемый параметр searchBy - " + searchBy + ".");
                }
                break;
            case 2:
                filmRows = jdbcTemplate.queryForRowSet(stmtForDirectorAndTitle, query, query);
                break;
            default:
                throw new IllegalArgumentException("Необрабатываемый параметр searchBy - " + searchBy + ".");
        }
        return fillListWithFilms(filmRows);
    }

    /**
     * 1) USER_LIKES - Лайки установленные пользователем
     * 2) INTERSECTED - Пользователи с количеством совпадений по лайкам
     * 3) RELATED_USER - Пользователь с наибольшим количеством совпадений по лайкам
     * 4) RECOMENDED - Итоговый набор с рекомендуемыми фильмами
     **/
    @Override
    public List<Film> getRecommendationsByUser(int userId) {
        final String sql =
                "SELECT FILMS.*,COUNT(LIKES.like_id) as count_like FROM films_likes AS RECOMENDED " +
                        "LEFT JOIN films_model as FILMS on RECOMENDED.film_id = FILMS.film_id " +
                        "LEFT JOIN films_likes AS LIKES on FILMS.film_id = LIKES.film_id " +
                        "WHERE " +
                        "       RECOMENDED.user_id in " +
                        "           (SELECT RELATED_USER.user_id FROM " +
                        "               (SELECT INTERSECTED.user_id, COUNT(FILM_ID) AS INTERSECTIONS_COUNT FROM films_likes AS INTERSECTED " +
                        "               WHERE INTERSECTED.user_id != ? and  INTERSECTED.film_id IN  (SELECT FILM_ID FROM films_likes AS USER_LIKES WHERE user_id = ?) " +
                        "               GROUP BY INTERSECTED.user_id ORDER BY INTERSECTIONS_COUNT DESC) " +
                        "           AS RELATED_USER LIMIT 1) " +
                        "   AND " +
                        "       RECOMENDED.FILM_ID NOT IN (SELECT FILM_ID FROM films_likes AS USER_LIKES WHERE user_id = ?) " +
                        "GROUP BY FILMS.film_id ";
        return fillListWithFilms(jdbcTemplate.queryForRowSet(sql, userId, userId, userId));
    }
}