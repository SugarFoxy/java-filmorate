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
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.film.FilmMapper;
import ru.yandex.practicum.filmorate.utils.film.FilmUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private GenreStorage genreStorage;

    @Autowired
    private MpaStorage mpaStorage;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private LikesStorage likesStorage;

    @Autowired
    private FilmUtils filmUtils;

    private Film film;
    private Genre genreComedy;
    private Genre genreDrama;
    private Mpa gMpa;
    private Mpa pgMpa;

    @BeforeEach
    public void beforeEach() {
        genreComedy = genreStorage.getGenreById(1);
        genreDrama = genreStorage.getGenreById(2);
        gMpa = mpaStorage.getMpaById(1);
        pgMpa = mpaStorage.getMpaById(2);
        film = new Film("Название", "Описание",
                LocalDate.of(2000, 1, 1), 30L, gMpa);
    }

    @Test
    @Sql("classpath:data.sql")
    public void addFilm() {
        Set<Genre> allGenres = new TreeSet<>(Comparator.comparing(Genre::getId));
        allGenres.add(genreComedy);
        allGenres.add(genreDrama);
        film.setGenres(allGenres);
        int id = filmStorage.addFilm(film).getId();
        String sqlQuery = "SELECT * FROM films_model WHERE film_id = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        filmRow.next();
        int filmId = filmRow.getInt("film_id");
        Film newFilm = FilmMapper.mapFilm(filmRow, filmUtils.getFilmMpa(filmId), filmUtils.getFilmGenres(filmId));
        assertEquals(film.getName(), newFilm.getName());
        assertEquals(film.getDescription(), newFilm.getDescription());
        assertEquals(film.getReleaseDate(), newFilm.getReleaseDate());
        assertEquals(film.getDuration(), newFilm.getDuration());
        assertEquals(film.getMpa(), newFilm.getMpa());
        Set<Genre> newAllGenres = newFilm.getGenres();
        assertEquals(allGenres, newAllGenres);
    }

    @Test
    @Sql("classpath:data.sql")
    public void deleteFilm() {
        int id = filmStorage.addFilm(film).getId();
        filmStorage.deleteFilm(id);
        String sqlQuery = "SELECT * FROM films_model WHERE film_id = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        assertFalse(filmRow.next());
        assertThrows(EntityNotFoundException.class, () -> filmStorage.deleteFilm(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void updateFilm() {
        Film newFilm = new Film("Новое название", "Новое описание",
                LocalDate.of(2005, 12, 12), 60L, pgMpa);
        Set<Genre> allGenres = new TreeSet<>(Comparator.comparing(Genre::getId));
        allGenres.add(genreComedy);
        newFilm.setGenres(allGenres);
        int id = filmStorage.addFilm(film).getId();
        newFilm.setId(id);
        filmStorage.updateFilm(newFilm);
        String sqlQuery = "SELECT * FROM films_model WHERE film_id = ?";
        SqlRowSet updatedFilmRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        updatedFilmRow.next();
        int filmId = filmUtils.getFilmId(updatedFilmRow);
        int mpaId = filmUtils.getFilmMpaId(updatedFilmRow);
        Film updatedFilm = FilmMapper.mapFilm(updatedFilmRow, filmUtils.getFilmMpa(mpaId), filmUtils.getFilmGenres(filmId));
        assertNotEquals(film.getName(), updatedFilm.getName());
        assertNotEquals(film.getDescription(), updatedFilm.getDescription());
        assertNotEquals(film.getReleaseDate(), updatedFilm.getReleaseDate());
        assertNotEquals(film.getMpa(), updatedFilm.getMpa());
        assertNotEquals(film.getGenres(), updatedFilm.getGenres());
        newFilm.setId(9999);
        assertThrows(EntityNotFoundException.class, () -> filmStorage.updateFilm(newFilm));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getFilmById() {
        filmStorage.addFilm(film);
        Film film = filmStorage.getFilmById(1);
        assertEquals(1, film.getId());
        assertThrows(EntityNotFoundException.class, () -> filmStorage.getFilmById(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getAllFilms() {
        filmStorage.addFilm(film);
        filmStorage.addFilm(film);
        filmStorage.addFilm(film);
        assertEquals(3, filmStorage.getAllFilms().size());
    }

    @Test
    @Sql("classpath:data.sql")
    public void getMostLikedFilms() {
        User firstUser = userStorage.addUser(new User("e5k4p3@gmail.com", "e5k4p3", "e5k4p3",
                LocalDate.of(1995, 7, 11)));
        User secondUser = userStorage.addUser(new User("mulenas@gmail.com", "Mulenas", "Mulenas",
                LocalDate.of(1995, 7, 11)));
        Film secondFilm = new Film("Второй", "Описание второго",
                LocalDate.of(1999, 8, 15), 50L, gMpa);
        Film thirdFilm = new Film("Третий", "Описание третьего",
                LocalDate.of(2007, 4, 7), 50L, pgMpa);
        filmStorage.addFilm(film);
        int secondFilmId = filmStorage.addFilm(secondFilm).getId();
        int thirdFilmId = filmStorage.addFilm(thirdFilm).getId();
        likesStorage.addLikeToFilm(thirdFilmId, firstUser.getId());
        likesStorage.addLikeToFilm(thirdFilmId, secondUser.getId());
        likesStorage.addLikeToFilm(secondFilmId, firstUser.getId());
        List<Film> topFilms = filmStorage.getMostLikedFilms(10);
        System.out.println(topFilms.size());
        System.out.println(topFilms);
        assertEquals("Третий", topFilms.get(0).getName());
        assertEquals("Второй", topFilms.get(1).getName());
        assertEquals("Название", topFilms.get(2).getName());
    }

    @Test
    @Sql("classpath:data.sql")
    public void getCommonFilms() {
        User firstUser = userStorage.addUser(new User("e5k4p3@gmail.com", "e5k4p3", "e5k4p3",
                LocalDate.of(1995, 7, 11)));
        User secondUser = userStorage.addUser(new User("mulenas@gmail.com", "Mulenas", "Mulenas",
                LocalDate.of(1995, 7, 11)));
        Film secondFilm = new Film("Второй", "Описание второго",
                LocalDate.of(1999, 8, 15), 50L, gMpa);
        Film thirdFilm = new Film("Третий", "Описание третьего",
                LocalDate.of(2007, 4, 7), 50L, pgMpa);
        filmStorage.addFilm(film);
        int secondFilmId = filmStorage.addFilm(secondFilm).getId();
        int thirdFilmId = filmStorage.addFilm(thirdFilm).getId();
        likesStorage.addLikeToFilm(secondFilmId, firstUser.getId());
        likesStorage.addLikeToFilm(thirdFilmId, firstUser.getId());
        likesStorage.addLikeToFilm(thirdFilmId, secondUser.getId());
        List<Film> commonFilms = filmStorage.getCommonFilms(firstUser.getId(), secondUser.getId());
        System.out.println(commonFilms.size());
        System.out.println(commonFilms);
        assertEquals("Третий", commonFilms.get(0).getName());
        assertEquals(1, commonFilms.size());
    }

}