package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    private int createId() {
        return id++;
    }

    public Map<Integer, User> getMapUsers() {
        return users;
    }

    @Override
    public List<User> getUsers() {
        Collection<User> value = users.values();
        return new ArrayList<>(value);
    }

    @Override
    public User addUser(User user) {
        user.setId(createId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void updateUsers(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new AbsenceOfObjectException("Такого пользователя нет");
        }
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }
}
