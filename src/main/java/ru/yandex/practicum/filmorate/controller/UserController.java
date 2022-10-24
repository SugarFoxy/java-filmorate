package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService =userService;
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
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getUsersFriends(@PathVariable Integer id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFromFriends(id, friendId);
    }
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handle(final MethodArgumentNotValidException e) {
        String[] allErrors = e.getAllErrors().toString().split(";");
        String massage = allErrors[allErrors.length-1];
        Map<String,String> map = Map.of("error", massage);
        log.warn(massage);
        return map;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handle(final ValidationException e) {
        Map<String,String> map = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return map;
    }
}
