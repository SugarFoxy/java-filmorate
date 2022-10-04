package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootApplication
class UserControllerTest {

    private ConfigurableApplicationContext context;
    private final URI url = URI.create("http://localhost:8080/users");

    @BeforeEach
    public void init() {
        context = SpringApplication.run(FilmorateApplication.class);
    }

    @Test
    void postUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String bodyBefore = response.body();


        String json = "{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"oks.94@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";

        final HttpRequest.BodyPublisher bodyNullLogin = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": null,\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}");
        HttpRequest requestNullLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyNullLogin).build();
        HttpResponse<String> responseNullLogin = client.send(requestNullLogin, HttpResponse.BodyHandlers.ofString());
        int statusNullLogin = responseNullLogin.statusCode();

        final HttpRequest.BodyPublisher bodyEmptyLogin = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\":  \" \",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}");
        HttpRequest requestEmptyLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyEmptyLogin)
                .build();
        HttpResponse<String> responseEmptyLogin = client.send(requestEmptyLogin, HttpResponse.BodyHandlers.ofString());
        int statusEmptyLogin = responseEmptyLogin.statusCode();

        final HttpRequest.BodyPublisher bodyEmptyEmail = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\":  \"login\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}");
        HttpRequest requestEmptyEmail = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyEmptyEmail)
                .build();
        HttpResponse<String> responseEmptyEmail = client.send(requestEmptyEmail, HttpResponse.BodyHandlers.ofString());
        int statusEmptyEmail = responseEmptyEmail.statusCode();

        final HttpRequest.BodyPublisher bodyNullEmail = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\":  \"login\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": null,\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}");
        HttpRequest requestNullEmail = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyNullEmail)
                .build();
        HttpResponse<String> responseNullEmail = client.send(requestNullEmail, HttpResponse.BodyHandlers.ofString());
        int statusNullEmail = responseNullEmail.statusCode();

        final HttpRequest.BodyPublisher bodyIncorrectEmail = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\":  \"login\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"hszdzh\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}");
        HttpRequest requestIncorrectEmail = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyIncorrectEmail)
                .build();
        HttpResponse<String> responseIncorrectEmail = client.send(requestIncorrectEmail, HttpResponse.BodyHandlers.ofString());
        int statusIncorrectEmail = responseIncorrectEmail.statusCode();

        final HttpRequest.BodyPublisher bodyIncorrectDate = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"2050-08-20\"\n" +
                "}");
        HttpRequest requestIncorrectDate = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyIncorrectDate)
                .build();
        HttpResponse<String> responseIncorrectDate = client.send(requestIncorrectDate, HttpResponse.BodyHandlers.ofString());
        int statusIncorrectDate = responseIncorrectDate.statusCode();

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String bodyAfterIncorrectBodyRequest = response1.body();
        int statusAfterIncorrectBodyRequest = response1.statusCode();

        final HttpRequest.BodyPublisher bodyCorrect = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestCorrect = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyCorrect)
                .build();
        HttpResponse<String> responseCorrect = client.send(requestCorrect, HttpResponse.BodyHandlers.ofString());
        int statusCorrect = responseCorrect.statusCode();

        final HttpRequest.BodyPublisher bodyCorrectNullName = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"oks\",\n" +
                "  \"name\": null,\n" +
                "  \"email\": \"oks.94@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}");
        HttpRequest requestCorrectNullName = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyCorrectNullName)
                .build();
        HttpResponse<String> responseCorrectNullName = client.send(requestCorrectNullName, HttpResponse.BodyHandlers.ofString());
        int statusCorrectNullName = responseCorrectNullName.statusCode();

        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String bodyAfter = response2.body();
        int statusAfter = response2.statusCode();

        assertAll(
                () -> assertEquals("[]", bodyBefore, "тело GET запроса должно быть пустое"),
                () -> assertEquals(400, statusNullLogin, "Логин не должен быть null, статус не 200"),
                () -> assertEquals(400, statusEmptyLogin, "Логин не должен быть пустым, статус не 200"),
                () -> assertEquals(400, statusEmptyEmail, "Email не должен быть пустым, статус не 200"),
                () -> assertEquals(400, statusNullEmail, "Email не должен быть null, статус не 200"),
                () -> assertEquals(400, statusIncorrectEmail, "Email должен быть существующим, статус не 200"),
                () -> assertEquals(500, statusIncorrectDate, "День рождения не должен быть в будующем, статус не 200"),
                () -> assertEquals("[]", bodyAfterIncorrectBodyRequest, "Корректных данных не поступало список пользователь пуст"),
                () -> assertEquals(200, statusAfterIncorrectBodyRequest, "на статус GET некорректные данные не повлияли"),
                () -> assertEquals(200, statusCorrect, "тело запроса коректно статус 200"),
                () -> assertEquals(200, statusCorrectNullName, "тело запроса коректно но без имени статус 200"),
                () -> assertEquals("[{\"id\":1,\"email\":\"oks.94@mail.ru\",\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"},{\"id\":2,\"email\":\"oks.94@mail.ru\",\"login\":\"oks\",\"name\":\"oks\",\"birthday\":\"1946-08-20\"}]", bodyAfter, "тело ответа состаит из двух элементов"),
                () -> assertEquals(200, statusAfter, "Запрос GET должен быть успешен 200")
                );
    }

    @Test
    void getUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String bodyBefore = response.body();
        int statusBefore = response.statusCode();

        String json = "{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"oks.94@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";

        final HttpRequest.BodyPublisher bodyCorrect = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestCorrect = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(bodyCorrect)
                .build();
        HttpResponse<String> responseCorrect = client.send(requestCorrect, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String bodyAfter = response2.body();
        int statusAfter = response2.statusCode();

        assertAll(
                () -> assertEquals("[]", bodyBefore, "тело GET запроса должно быть пустое"),
                () -> assertEquals(200, statusBefore, "Запрос GET должен быть успешен 200"),
                () -> assertEquals("[{\"id\":1,\"email\":\"oks.94@mail.ru\",\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"}]", bodyAfter, "тело ответа состоит из двух элементов"),
                () -> assertEquals(200, statusAfter, "Запрос GET должен быть успешен 200")
        );

    }

    @Test
    void updateUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String bodyBefore = response.body();

        String jsonForPost = "{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"oks.94@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonForPost);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        client.send(requestPost, HttpResponse.BodyHandlers.ofString());


        String jsonForPut = "{\n" +
                "  \"login\": \"doloreUpdate\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}";


        final HttpRequest.BodyPublisher bodyNullLogin = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": null,\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}");
        HttpRequest requestNullLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyNullLogin).build();
        HttpResponse<String> responseNullLogin = client.send(requestNullLogin, HttpResponse.BodyHandlers.ofString());
        int statusNullLogin = responseNullLogin.statusCode();

        final HttpRequest.BodyPublisher bodyEmptyLogin = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}");
        HttpRequest requestEmptyLogin = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyEmptyLogin)
                .build();
        HttpResponse<String> responseEmptyLogin = client.send(requestEmptyLogin, HttpResponse.BodyHandlers.ofString());
        int statusEmptyLogin = responseEmptyLogin.statusCode();

        final HttpRequest.BodyPublisher bodyEmptyEmail = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"login\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}");
        HttpRequest requestEmptyEmail = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyEmptyEmail)
                .build();
        HttpResponse<String> responseEmptyEmail = client.send(requestEmptyEmail, HttpResponse.BodyHandlers.ofString());
        int statusEmptyEmail = responseEmptyEmail.statusCode();

        final HttpRequest.BodyPublisher bodyNullEmail = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"login\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": null,\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}");
        HttpRequest requestNullEmail = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyNullEmail)
                .build();
        HttpResponse<String> responseNullEmail = client.send(requestNullEmail, HttpResponse.BodyHandlers.ofString());
        int statusNullEmail = responseNullEmail.statusCode();

        final HttpRequest.BodyPublisher bodyIncorrectEmail = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"login\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"shaerh\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}");
        HttpRequest requestIncorrectEmail = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyIncorrectEmail)
                .build();
        HttpResponse<String> responseIncorrectEmail = client.send(requestIncorrectEmail, HttpResponse.BodyHandlers.ofString());
        int statusIncorrectEmail = responseIncorrectEmail.statusCode();

        final HttpRequest.BodyPublisher bodyIncorrectDate = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"login\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"2050-09-20\"\n" +
                "}");
        HttpRequest requestIncorrectDate = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyIncorrectDate)
                .build();
        HttpResponse<String> responseIncorrectDate = client.send(requestIncorrectDate, HttpResponse.BodyHandlers.ofString());
        int statusIncorrectDate = responseIncorrectDate.statusCode();

        final HttpRequest.BodyPublisher bodyIncorrectId = HttpRequest.BodyPublishers.ofString("{\n" +
                "  \"login\": \"login\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"id\": 99999,\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}");
        HttpRequest requestIncorrectId = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyIncorrectId)
                .build();
        HttpResponse<String> responseIncorrectId = client.send(requestIncorrectId, HttpResponse.BodyHandlers.ofString());
        int statusIncorrectId = responseIncorrectId.statusCode();

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String bodyAfterIncorrectUpdate = response1.body();

        final HttpRequest.BodyPublisher bodyCorrect = HttpRequest.BodyPublishers.ofString(jsonForPut);
        HttpRequest requestCorrect = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(bodyCorrect)
                .build();
        HttpResponse<String> responseCorrect = client.send(requestCorrect, HttpResponse.BodyHandlers.ofString());
        int statusCorrect= responseCorrect.statusCode();

        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String bodyAfter= response2.body();

        assertAll(
                () -> assertEquals("[]", bodyBefore, "тело GET запроса должно быть пустое"),
                () -> assertEquals(400, statusNullLogin, "Логин не должен быть null, статус не 200"),
                () -> assertEquals(400, statusEmptyLogin, "Логин не должен быть пустым, статус не 200"),
                () -> assertEquals(400, statusEmptyEmail, "Email не должен быть пустым, статус не 200"),
                () -> assertEquals(400, statusNullEmail, "Email не должен быть null, статус не 200"),
                () -> assertEquals(400, statusIncorrectEmail, "Email должен быть существующим, статус не 200"),
                () -> assertEquals(500, statusIncorrectDate, "День рождения не должен быть в будующем, статус не 200"),
                () -> assertEquals("[{\"id\":1,\"email\":\"oks.94@mail.ru\",\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"}]", bodyAfterIncorrectUpdate, "Корректных данных не поступало список пользователь пуст"),
                () -> assertEquals(200, statusCorrect, "тело запроса коректно статус 200"),
                () -> assertEquals("[{\"id\":1,\"email\":\"mail@yandex.ru\",\"login\":\"doloreUpdate\",\"name\":\"est adipisicing\",\"birthday\":\"1976-09-20\"}]", bodyAfter, "тело ответа состоит из одного элемента"),
                () -> assertEquals(500, statusIncorrectId, "такого id не существует")
        );

    }

    @AfterEach
    public void close() {
        SpringApplication.exit(context);
    }
}