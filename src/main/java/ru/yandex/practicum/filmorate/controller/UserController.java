package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private static final LocalDate REFERENCE_POINT_RELEASE_DATE = LocalDate.now();
    private int id = 0;


    public int createId() {
        return id++;
    }

    @GetMapping("/films")
    public Map<Integer, User> getUsers() {
        log.info("Получен запрос на список пользователей");
        return users;
    }

    @PostMapping("/film")
    public User postFilm(@RequestBody User user) {
        try {
            validation(user);
            user.setId(createId());
            users.put(user.getId(), user);
            log.info("Пользователь "+user.getLogin()+" добавлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return user;
    }

    @PutMapping("/film")
    public User updateFilm(@RequestBody User user) {
        try {
            validation(user);
            users.replace(user.getId(), user);
            log.info("Пользователь "+user.getLogin()+" обновлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return user;
    }

    private void validation(User user) throws ValidationException {
        if (user.getEmail().isBlank() && !user.getEmail().contains("@") ) {
            throw new ValidationException("Email не может быть пустым и должен иметь самвол '@'");
        }
        if (user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(REFERENCE_POINT_RELEASE_DATE)) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
