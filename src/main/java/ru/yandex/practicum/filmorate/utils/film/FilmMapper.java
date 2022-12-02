package ru.yandex.practicum.filmorate.utils.film;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

@UtilityClass
public class FilmMapper {
    public static Film mapFilm(SqlRowSet filmRow, Mpa filmMpa, Set<Genre> filmGenres) {
        Film film = new Film(filmRow.getString("title"),
                filmRow.getString("description"),
                filmRow.getDate("release_date").toLocalDate(),
                filmRow.getLong("duration"),
                filmMpa);
        film.setId(filmRow.getInt("film_id"));
        film.setGenres(filmGenres);
        return film;
    }
}
