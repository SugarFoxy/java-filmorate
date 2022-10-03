package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")

@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private static final LocalDate NOW_DATE = LocalDate.now();
    private int id = 1;


    private int createId() {
        return id++;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на список пользователей");
        return getListUsers();
    }

    @PostMapping
    public User postUsers(@Valid @RequestBody User user) {
        try {
            user.setId(createId());
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getLogin() + " добавлен");
        } catch (RuntimeException e ) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return user;
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        try {
            if (users.containsKey(user.getId())) {
                users.replace(user.getId(), user);
                log.info("Пользователь " + user.getLogin() + " обновлен");
            } else {
                throw new ValidationException("Такого пользователя не существует");
            }
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e);
        }
        return user;
    }

    private List<User> getListUsers() {
        Collection<User> value = users.values();
        return new ArrayList<>(value);
    }
}
