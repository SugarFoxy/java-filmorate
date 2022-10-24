package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    private static final LocalDate NOW_DATE = LocalDate.now();
    private int id = 1;


    private int createId() {
        return id++;
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
        }else {
            throw new ValidationException("Такого пользователя нет");
        }
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }
}
