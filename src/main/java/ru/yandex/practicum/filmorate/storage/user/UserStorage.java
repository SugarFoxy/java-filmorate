package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> getUsers();
    public User postUsers(User user);
    public User updateUsers(User user);
    public User getUserById(Integer id );
}
