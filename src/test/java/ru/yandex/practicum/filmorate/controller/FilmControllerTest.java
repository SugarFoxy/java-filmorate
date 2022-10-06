package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    final Film filmNullName = new Film(null, "Test description", LocalDate.of(1994, 5, 21), 100);
    final Film filmEmptyName = new Film("", "Test description", LocalDate.of(1994, 5, 21), 100);
    final Film filmEarlyRelease = new Film("TestName", "Test description", LocalDate.of(1895, 12, 27), 100);
    final Film filmNegativeDuration = new Film("TestName", "Test description", LocalDate.of(1994, 5, 21), -1);
    final Film film201description = new Film("TestName", "Test description Test description " +
            "Test description Test description Test description Test description Test description " +
            "Test description Test description Test description Test description Test descripti",
            LocalDate.of(1994, 5, 21),
            100
    );
    Film film = new Film("TestName",
            "Test description Test description Test description Test description Test description " +
                    "Test description Test description Test description Test description Test description " +
                    "Test description Test descript",
            LocalDate.of(1895, 12, 28),
            10
    );

    @BeforeEach
    public void init(){
        filmController = new FilmController();
    }

    @Test
    void getFilms() {
        List<Film> filmsBefore = filmController.getFilms();
        filmController.postFilm(film);
        List<Film> filmsAfter = filmController.getFilms();
        assertAll(
                () -> assertEquals(0, filmsBefore.size(), "список пуст"),
                () -> assertEquals(1, filmsAfter.size(), "имеется оди элемент")
        );
    }

    @Test
    void postFilm(){
        List<Film> filmsBefore = filmController.getFilms();

        assertThrows(ValidationException.class,  () -> filmController.postFilm(filmNullName), "название не может быть пустым");
        assertThrows(ValidationException.class,  () -> filmController.postFilm(filmEmptyName),"название не может быть пустым");
        assertThrows(ValidationException.class,  () -> filmController.postFilm(film201description),"максимальная длина описания — 200 символов");
        assertThrows(ValidationException.class,  () -> filmController.postFilm(filmEarlyRelease),"дата релиза — не раньше 28 декабря 1895");
        assertThrows(ValidationException.class,  () -> filmController.postFilm(filmNegativeDuration),"продолжительность фильма должна быть положительной");

        film.setId(5);
        assertThrows(ValidationException.class,  () -> filmController.updateFilm(film),"Такого фильма не существует");

        List<Film> filmsAfterIncorrectAddition = filmController.getFilms();

        filmController.postFilm(film);
        filmController.updateFilm(film);

        List<Film> filmsAfter = filmController.getFilms();

        assertAll(
                () -> assertEquals(0, filmsBefore.size(), "список пуст"),
                () -> assertEquals(0, filmsAfterIncorrectAddition.size(), "список пуст"),
                () -> assertEquals(1, filmsAfter.size(), "имеется оди элемент")
        );

    }

    @Test
    void updateFilm() {
        List<Film> filmsBefore = filmController.getFilms();

        assertThrows(ValidationException.class,  () -> filmController.updateFilm(filmNullName), "название не может быть пустым");
        assertThrows(ValidationException.class,  () -> filmController.updateFilm(filmEmptyName),"название не может быть пустым");
        assertThrows(ValidationException.class,  () -> filmController.updateFilm(film201description),"максимальная длина описания — 200 символов");
        assertThrows(ValidationException.class,  () -> filmController.updateFilm(filmEarlyRelease),"дата релиза — не раньше 28 декабря 1895");
        assertThrows(ValidationException.class,  () -> filmController.updateFilm(filmNegativeDuration),"продолжительность фильма должна быть положительной");

        List<Film> filmsAfterIncorrectAddition = filmController.getFilms();

        filmController.postFilm(film);

        List<Film> filmsAfter = filmController.getFilms();
        assertAll(
                () -> assertEquals(0, filmsBefore.size(), "список пуст"),
                () -> assertEquals(0, filmsAfterIncorrectAddition.size(), "список пуст"),
                () -> assertEquals(1, filmsAfter.size(), "имеется оди элемент")
        );


    }
}