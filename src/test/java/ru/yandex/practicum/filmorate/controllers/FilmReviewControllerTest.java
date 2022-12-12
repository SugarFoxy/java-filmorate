package ru.yandex.practicum.filmorate.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class FilmReviewControllerTest {

    private final MockMvc mockMvc;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private Gson gson;
    private User userU;
    private Film filmF;
    private FilmReviewDto correctDto;
    private FilmReviewDto blankContentDto;
    private FilmReviewDto nullIsPositiveBoolean;
    private FilmReviewDto emptyUserIdDto;
    private FilmReviewDto emptyFilmIdDto;

    @Autowired
    public FilmReviewControllerTest(MockMvc mockMvc, UserStorage userStorage, FilmStorage filmStorage) {
        this.mockMvc = mockMvc;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @BeforeEach
    public void beforeEach() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        userU = new User("erew@gmail.com", "efgdsf", "Mulууenas", LocalDate.of(1996, 8, 13));
        filmF = new Film("Название фильма", "Описание фильма",
                LocalDate.of(2000, 10, 10), 100L, new Mpa(1, null, null));
        correctDto = new FilmReviewDto("content", true, 1, 1, 0);
        blankContentDto = new FilmReviewDto("", true, 1, 1, 0);
        nullIsPositiveBoolean = new FilmReviewDto("content", null, 1, 1, 0);
        emptyFilmIdDto = new FilmReviewDto("content", true, null, 1, 0);
        emptyUserIdDto = new FilmReviewDto("content", true, 1, null, 0);
    }

    @Test
    @DisplayName("проверяет валидность данных")
    public void validationTests() throws Exception {
        filmStorage.addFilm(filmF);
        userStorage.addUser(userU);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(correctDto)))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(blankContentDto)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(nullIsPositiveBoolean)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(nullIsPositiveBoolean)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(emptyFilmIdDto)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(emptyUserIdDto)))
                .andExpect(status().is4xxClientError());

    }
}
