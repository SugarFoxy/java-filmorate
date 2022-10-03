package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void getFilms() {
        FilmController filmController = new FilmController();
        Film film = new Film("TestName", "Test description", LocalDate.of(1994, 5, 21), 100);
        List<Film> filmsBefore = filmController.getFilms();
        filmController.postFilm(film);
        List<Film> filmsAfter = filmController.getFilms();
        assertAll(
                () -> assertEquals(0, filmsBefore.size(), "список пуст"),
                () -> assertEquals(1, filmsAfter.size(), "имеется оди элемент")
        );
    }

        @Test
        void postFilm () {
        }

        @Test
        void updateFilm () {
        }
    }