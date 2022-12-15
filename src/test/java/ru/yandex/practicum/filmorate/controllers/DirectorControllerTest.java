package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Director;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class DirectorControllerTest {
    private final MockMvc mockMvc;
    private Gson gson;
    private Director correctDirector;
    private Director incorrectDirector;

    @Autowired
    public DirectorControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void beforeEach() {
        gson = new Gson();
        correctDirector = new Director(1, "Имя Режиссера");
        incorrectDirector = new Director(2, "");
    }

    @Test
    public void validationTest() throws Exception {
        mockMvc.perform(post("/directors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(correctDirector)))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/directors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(incorrectDirector)))
                .andExpect(status().is4xxClientError());
    }
}
