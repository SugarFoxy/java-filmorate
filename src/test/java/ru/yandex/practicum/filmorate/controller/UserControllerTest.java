package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controller.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest {

    private ConfigurableApplicationContext context;
    private final URI url = URI.create("http://localhost:8080/users");
    private User nullLogin;
    private User nullEmail;
    private User incorrectLogin;
    private User incorrectBirthday;
    private User incorrectEmail;
    private User nullName;
    private User nonexistentId;

    @BeforeEach
    public void init() {
        context = SpringApplication.run(FilmorateApplication.class);
        nullLogin = new User(null, "mail@mail.ru", null, "Nick Name", LocalDate.of(1946, 8, 20));
        nullEmail = new User(null, null, null, "dolore", LocalDate.of(1946, 8, 20));
        incorrectLogin = new User(1, "mail@mail.ru", "dol ore", "Nick Name", LocalDate.of(1946, 8, 20));
        incorrectBirthday = new User(null, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(2050, 8, 20));
        incorrectEmail = new User(null, "mailmail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        nullName = new User(1, "mail@mail.ru", "dolore", null, LocalDate.of(1946, 8, 20));
        nonexistentId = new User(9999, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
    }

    private int postToServer(User user) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();

        String userSerialized = gson.toJson(user);

        HttpClient client = HttpClient.newHttpClient();

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(userSerialized);

        HttpRequest requestNullLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body).build();
        HttpResponse<String> responseNullLogin = client.send(requestNullLogin, HttpResponse.BodyHandlers.ofString());
        return responseNullLogin.statusCode();
    }

    private int putToServer(User user) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        String userSerialized = gson.toJson(user);

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
                () -> assertEquals(400, postToServer(nullLogin), "Логин не должен быть null, статус 400"),
                () -> assertEquals(400, postToServer(incorrectLogin), "Логин не должен содержать пробелы, статус 400"),
                () -> assertEquals(400, postToServer(incorrectEmail), "Email некоррекный, статус 400"),
                () -> assertEquals(400, postToServer(nullEmail), "Email не должен быть null, статус 400"),
                () -> assertEquals(400, postToServer(incorrectBirthday), "День рождения не должен быть в будующем, статус 400"),
                () -> assertEquals(200, postToServer(nullName), "Имя может быть пустым, статус 200")
        );
    }

    @Test
    void putUsers() throws IOException, InterruptedException {
        postToServer(nullName);
        assertAll(
                () -> assertEquals(400, putToServer(nullLogin), "Логин не должен быть null, статус 400"),
                () -> assertEquals(400, putToServer(incorrectLogin), "Логин не должен содержать пробелы, статус 400"),
                () -> assertEquals(400, putToServer(incorrectEmail), "Email некоррекный, статус 400"),
                () -> assertEquals(400, putToServer(nullEmail), "Email не должен быть null, статус 400"),
                () -> assertEquals(400, putToServer(incorrectBirthday), "День рождения не должен быть в будующем, статус 400"),
                () -> assertEquals(200, putToServer(nullName), "Имя может быть пустым, статус 200"),
                () -> assertEquals(404, putToServer(nonexistentId), "Такого пользователя не существует, статус 404")
        );
    }

    @AfterEach
    @Sql(value = {"create-Users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void close() {
        SpringApplication.exit(context);
    }
}