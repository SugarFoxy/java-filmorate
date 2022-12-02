package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);
    void deleteUser(int id);
    User updateUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
}
