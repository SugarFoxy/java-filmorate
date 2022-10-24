package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")

@Slf4j
public class UserController {
    InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на список пользователей");
        return inMemoryUserStorage.getUsers();
    }

    @PostMapping
    public User postUsers(@Valid @RequestBody User user) {
        try {
            inMemoryUserStorage.postUsers(user);
            log.info("Пользователь " + user.getLogin() + " добавлен");
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return user;
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        try {
            inMemoryUserStorage.updateUsers(user);
            log.info("Пользователь " + user.getLogin() + " обновлен");
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return user;
    }
}
