package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.service.FeedEventService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FeedEventService feedEventService;

    @Autowired
    public UserController(UserService userService, FeedEventService feedEventService) {
        this.userService = userService;
        this.feedEventService = feedEventService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        userService.logValidationErrors(bindingResult);
        return userService.addUser(user);
    }

    @DeleteMapping("/{userId}") //тут
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        userService.logValidationErrors(bindingResult);
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable int userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable int userId, @PathVariable int friendId) {
        return userService.getCommonFriends(userId, friendId);
    }

    @GetMapping("/{userId}/recommendations")
    public List<Film> getRecommendations(@PathVariable int userId) {
        return userService.getRecommendations(userId);
    }

    @GetMapping("/{userId}/feed")
    public List<FeedEvent> getFeed(@PathVariable int userId) {
        return feedEventService.getFeed(userId);
    }
}