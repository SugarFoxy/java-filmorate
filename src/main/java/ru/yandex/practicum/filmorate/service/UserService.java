package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendsStorage friendsStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
        this.filmStorage = filmStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    } //тут

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        friendsStorage.addToFriends(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        friendsStorage.removeFromFriends(userId, friendId);
    }

    public List<User> getUserFriends(int userId) {
        return friendsStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        return friendsStorage.getCommonFriends(userId, otherId);
    }

    public void logValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                throw new ValidationException(error.getDefaultMessage());
            }
        }
    }

    public List<Film> getRecommendations(int userId) {
        return filmStorage.getRecommendationsByUser(userId);
    }
}
