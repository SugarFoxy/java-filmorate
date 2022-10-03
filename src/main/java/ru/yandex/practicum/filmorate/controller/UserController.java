package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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
    public User postUsers(@RequestBody User user) {
        try {
            validation(user);
            user.setId(createId());
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getLogin() + " добавлен");
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        return user;
    }

    @PutMapping
    public User updateUsers(@RequestBody User user) {
        try {
            validation(user);
            if (users.containsKey(user.getId())) {
                users.replace(user.getId(), user);
                log.info("Пользователь " + user.getLogin() + " обновлен");
            } else {
                throw new ValidationException("Такого пользователя не существует");
            }
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        return user;
    }

    private List<User> getListUsers() {
        Collection<User> value = users.values();
        return new ArrayList<>(value);
    }

    private void validation(User user) throws ValidationException {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(NOW_DATE)) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
