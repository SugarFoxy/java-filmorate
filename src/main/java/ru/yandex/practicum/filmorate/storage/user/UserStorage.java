package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> getUsers();
    User addUser(User user);
    void updateUsers(User user);
    User getUserById(Integer id );
    public Map<Integer,User> getMapUsers();
}
