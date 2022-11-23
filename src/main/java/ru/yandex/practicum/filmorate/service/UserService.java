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

    public List<User> getUser() {
        log.info("Получен запрос на список всех пользователей");
        return storage.getUsers();
    }

    public User createUser(User user) {
        log.info("Получен запрос на создание пользавателя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validLogin(user);
        return storage.addUser(user);
    }

    public User updateUser(User user) throws AbsenceOfObjectException {
        log.info("Получен запрос на изменение пользователя");
        validLogin(user);
        return storage.updateUser(user);
    }

    public User getUserById(Integer id) throws AbsenceOfObjectException {
        log.info("Получен запрос на получение пользователя по ID");
        return storage.getUserById(id);
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

    private void validLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }
}
