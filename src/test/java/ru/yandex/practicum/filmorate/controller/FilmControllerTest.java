package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controller.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {


    private ConfigurableApplicationContext context;
    private final URI url = URI.create("http://localhost:8080/films");
    private Film nullname;
    private Film incorrectDescription;
    private Film incorrectReleaseDate;
    private Film negativeDuration;
    private Film correctFilm;
    private Film nonexistentId;

    @BeforeEach
    public void init() {
        context = SpringApplication.run(FilmorateApplication.class);
        nullname = new Film(null, null, "Duis in consequat esse", LocalDate.of(1946, 8, 20),100, null);
        incorrectDescription = new Film(null, "labore nulla", "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.", LocalDate.of(1946, 8, 20),100, null);
        incorrectReleaseDate = new Film(null, "labore nulla", "Duis in consequat esse", LocalDate.of(1884, 8, 20),100, null);
        negativeDuration = new Film(null, "labore nulla", "Duis in consequat esse", LocalDate.of(1946, 8, 20),-3, null);
        correctFilm = new Film(1, "labore nulla", "Duis in consequat esse", LocalDate.of(1946, 8, 20),100, null);
        nonexistentId = new Film(9999, "labore nulla", "Duis in consequat esse", LocalDate.of(1946, 8, 20),100, null);
    }

    private int postToServer(Film film) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();

        String userSerialized = gson.toJson(film);

        HttpClient client = HttpClient.newHttpClient();

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(userSerialized);

        HttpRequest requestNullLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body).build();
        HttpResponse<String> responseNullLogin = client.send(requestNullLogin, HttpResponse.BodyHandlers.ofString());
        return responseNullLogin.statusCode();
    }

    private int putToServer(Film film) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        String userSerialized = gson.toJson(film);

        HttpClient client = HttpClient.newHttpClient();

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(userSerialized);

        HttpRequest requestNullLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(body).build();
        HttpResponse<String> responseNullLogin = client.send(requestNullLogin, HttpResponse.BodyHandlers.ofString());
        return responseNullLogin.statusCode();
    }


    @Test
    void postUsers() {
        assertAll(
                () -> assertEquals(400, postToServer(nullname), "Имя не должно быть null, статус 400"),
                () -> assertEquals(400, postToServer(incorrectDescription), "Описание не должно привышать 200 символов, статус 400"),
                () -> assertEquals(400, postToServer(incorrectReleaseDate), "релиз не может быть раньше 28.12.1895, статус 400"),
                () -> assertEquals(400, postToServer(negativeDuration), "продолжительность не может быть отрицательной, статус 400"),
                () -> assertEquals(200, postToServer(correctFilm), "Фильм создан верно, статус 200")
        );
    }

    @Test
    void putUsers() throws IOException, InterruptedException {
        postToServer(correctFilm);
        assertAll(
                () -> assertEquals(400, putToServer(nullname), "Имя не должно быть null, статус 400"),
                () -> assertEquals(400, putToServer(incorrectDescription), "Описание не должно привышать 200 символов, статус 400"),
                () -> assertEquals(400, putToServer(incorrectReleaseDate), "релиз не может быть раньше 28.12.1895, статус 400"),
                () -> assertEquals(400, putToServer(negativeDuration), "продолжительность не может быть отрицательной, статус 400"),
                () -> assertEquals(404, putToServer(nonexistentId), "Фильм не существует, статус 404"),
                () -> assertEquals(200, putToServer(correctFilm), "Фильм создан верно, статус 200")
        );
    }

    @AfterEach
    public void close() {
        SpringApplication.exit(context);
    }
}