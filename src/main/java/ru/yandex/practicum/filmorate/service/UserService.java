package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.frends.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage,
                       @Qualifier("friendDbStorage") FriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
    }

    public List<User> getUsers() {
        log.info("Получен запрос на список всех пользователей");
        List<User> users = storage.getUsers();
        users.forEach(user -> user.setFriends(friendStorage.getAllFriendByUser(user.getId())));
        return users;
    }

    public User createUser(User user) {
        log.info("Получен запрос на создание пользавателя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validLogin(user);
        User createdUser =storage.addUser(user);
        createdUser.setFriends(friendStorage.getAllFriendByUser(createdUser.getId()));
        return createdUser;
    }

    public User updateUser(User user) throws AbsenceOfObjectException {
        log.info("Получен запрос на изменение пользователя");
        validLogin(user);
        User changedUser =storage.updateUser(user);
        changedUser.setFriends(friendStorage.getAllFriendByUser(changedUser.getId()));
        return changedUser;
    }

    public User getUserById(Integer id) throws AbsenceOfObjectException {
        log.info("Получен запрос на получение пользователя по ID");
        User user = storage.getUserById(id);
        user.setFriends(friendStorage.getAllFriendByUser(id));
        return user;
    }

    public List<User> findAllFriends(Integer id) {
        log.info("Получен запрос на список друзей пользователя");
        List<User> users = new ArrayList<>();
        for (Integer friendId : friendStorage.getAllFriendByUser(id)) {
            users.add(storage.getUserById(friendId));
        }
        return users;
    }

    public void addToFriends(Integer id, Integer otherId) throws AbsenceOfObjectException {
        log.info("Получен запрос на добавление в друзья");
        friendStorage.addFriend(id, otherId);
    }


    public void removeFromFriends(Integer id, Integer otherId) {
        log.info("Получен запрос на удаление из друзей");
        friendStorage.deleteFriend(id, otherId);
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        log.info("Получен запрос на получение списка общих друзей");
        User user = storage.getUserById(id);
        user.setFriends(friendStorage.getAllFriendByUser(user.getId()));
        User friend = storage.getUserById(otherId);
        friend.setFriends(friendStorage.getAllFriendByUser(friend.getId()));
        if (user.getFriends() != null && friend.getFriends() != null) {
            return user.getFriends().stream()
                    .map(storage::getUserById)
                    .filter(u -> friend.getFriends().contains(u.getId()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void validLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }
}
