package ru.yandex.practicum.filmorate.utils.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.director.DirectorMapper;
import ru.yandex.practicum.filmorate.utils.genre.GenreMapper;
import ru.yandex.practicum.filmorate.utils.mpa.MpaMapper;

import java.util.*;

@Component
public class FilmUtils {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getFilmId(SqlRowSet filmRow) {
        return filmRow.getInt("film_id");
    }

    public int getFilmMpaId(SqlRowSet filmRow) {
        return filmRow.getInt("mpa_id");
    }

    public Mpa getFilmMpa(int mpaId) {
        String sqlQuery = "SELECT * FROM mpa_dictionary WHERE mpa_id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);
        if (mpaRow.next()) {
            return MpaMapper.mapMpa(mpaRow);
        } else {
            throw new EntityNotFoundException("MPA с id " + mpaRow.getInt("mpa_id") + " не найден.");
        }
    }

    public Set<Genre> getFilmGenres(int id) {
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        String sqlQuery = "SELECT * FROM genre_dictionary WHERE genre_id IN " +
                "(SELECT genre_id FROM FILMS_GENRES WHERE film_id = ?)";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (genreRows.next()) {
            filmGenres.add(GenreMapper.mapGenre(genreRows));
        }
        return filmGenres;
    }

    public List<Director> getFilmDirectors(int id) {
        List<Director> filmDirectors = new ArrayList<>();
        String sqlQuery = "SELECT * FROM directors_model WHERE director_id IN (SELECT director_id FROM films_directors WHERE film_id = ?)";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (directorRows.next()) {
            filmDirectors.add(DirectorMapper.mapDirector(directorRows));
        }
        return filmDirectors;
    }

    public SqlRowSet getSqlRowSetByFilmId(int id) {
        String sqlQuery = "SELECT * FROM films_model WHERE film_id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id);
    }
}
