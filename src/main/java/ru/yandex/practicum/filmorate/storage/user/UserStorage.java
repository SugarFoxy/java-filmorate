package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface UserStorage {
    List<User> getUsers();
    User addUser(User user);
    void updateUsers(User user);
    User getUserById(Integer id );
}
