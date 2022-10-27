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

    @Override
    public List<User> getUser() {
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
    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new AbsenceOfObjectException("Такого пользователя нет");
        }
    }

    @Override
    public User getUserById(Integer id) {
        if(users.containsKey(id)) {
            return users.get(id);
        }else {
           throw new  AbsenceOfObjectException("Такого пользователя нет");
        }
    }
}
