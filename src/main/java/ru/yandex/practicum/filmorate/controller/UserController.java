package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на список пользователей");
        return userService.getUsers();
    }

    @PostMapping
    public User addUsers(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользавателя");
        return userService.postUser(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        userService.updateUsers(user);
        log.info("пользователь изменен");
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUsersFriends(@PathVariable Integer id) {
        log.info("запрошен список друзей определеного пользователя");
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("запрошен список общих друзей");
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addToFriends(id, friendId);
        log.info("Друг добавлен");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFromFriends(id, friendId);
        log.info("Друг удален");
    }
}
