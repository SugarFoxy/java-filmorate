package ru.yandex.practicum.filmorate.dao.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    int addToFriends(int userId, int friendId);
    int removeFromFriends(int userId, int friendId);
    List<User> getUserFriends(int id);
    List<User> getCommonFriends(int userId, int otherId);
}
