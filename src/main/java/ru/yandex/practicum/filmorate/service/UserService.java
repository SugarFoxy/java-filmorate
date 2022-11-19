package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUser() {
        return storage.getUser();
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        return storage.addUser(user);
    }

    public void updateUser(User user) throws AbsenceOfObjectException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        storage.updateUser(user);
    }

    public User getUserById(Integer id) throws AbsenceOfObjectException {
        return storage.getUserById(id);
    }

    public List<User> findAllFriends(Integer id) {
        List<User> users = new ArrayList<>();
        for (Integer friendId : storage.getUserById(id).getFriends()) {
            users.add(storage.getUserById(friendId));
        }
        return users;
    }

    public void addToFriends(Integer id, Integer otherId) throws AbsenceOfObjectException {
            User user = storage.getUserById(id);
            User friend = storage.getUserById(otherId);
            user.addFriend(otherId);
            friend.addFriend(id);
    }


    public void removeFromFriends(Integer id, Integer otherId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(otherId);
        user.deleteFriend(otherId);
        friend.deleteFriend(id);
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(otherId);
        if (user.getFriends() != null && friend.getFriends() != null) {
            return user.getFriends().stream()
                    .map(storage::getUserById)
                    .filter(u -> friend.getFriends().contains(u.getId()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
