package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class UserControllerTest {

    private final MockMvc mockMvc;
    private Gson gson;
    private User correctUser;
    private User userWithIncorrectEmail;
    private User userWithIncorrectLogin;
    private User userWithEmptyName;
    private User userWithIncorrectBirthday;

    @Autowired
    UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void beforeEach() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        correctUser = new User("emr4@gmail.com", "euneoch", "Murlenassas", LocalDate.of(1995, 7, 11));
        userWithIncorrectEmail = new User("@gmail.com", "e5k4p3", "Mulenas", LocalDate.of(1995, 7, 11));
        userWithIncorrectLogin = new User("e5k4p3@gmail.com", "e5 k4 p3", "Mulenas", LocalDate.of(1995, 7, 11));
        userWithEmptyName = new User("m@gmail.com", "Mule", "", LocalDate.of(1995, 7, 11));
        userWithIncorrectBirthday = new User("e5k4p3@gmail.com", "e5k4p3", "Mulenas", LocalDate.of(2995, 7, 11));
    }

    @Test
    public void validationTest() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(correctUser)))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userWithIncorrectEmail)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userWithIncorrectLogin)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userWithEmptyName)))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userWithIncorrectBirthday)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(status().is4xxClientError());
    }
}