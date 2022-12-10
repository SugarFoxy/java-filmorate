package ru.yandex.practicum.filmorate.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class FilmReviewControllerTest {

    private final MockMvc mockMvc;
    private Gson gson;
    private User user;
    private Film film;
    private FilmReviewDto correctDto;
    private FilmReviewDto blankContentDto;
    private FilmReviewDto nullIsPositiveBoolean;
    private FilmReviewDto emptyUserIdDto;
    private FilmReviewDto emptyFilmIdDto;

    @Autowired
    public FilmReviewControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void beforeEach() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        user = new User("e@gmail.com", "e", "Mulenas", LocalDate.of(1995, 7, 11));
        film = new Film("Название фильма", "Описание фильма",
                LocalDate.of(2000, 10, 10), 100L, new Mpa(1, null, null));
        correctDto = new FilmReviewDto("content", true, 1, 1, 0);
        blankContentDto = new FilmReviewDto("", true, 1, 1, 0);
        nullIsPositiveBoolean = new FilmReviewDto("content", null, 1, 1, 0);
        emptyFilmIdDto =  new FilmReviewDto("content", true, null, 1, 0);
        emptyUserIdDto = new FilmReviewDto("content", true, 1, null, 0);
       // mockMvc.perform()


    }
}
