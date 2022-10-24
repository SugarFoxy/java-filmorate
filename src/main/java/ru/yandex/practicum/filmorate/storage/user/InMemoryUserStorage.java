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

    public List<User> getUsers() {
        Collection<User> value = users.values();
        return new ArrayList<>(value);
    }


    public User postUsers(User user) {
        try {
            validation(user);
            user.setId(createId());
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } catch (RuntimeException e) {
            throw new ValidationException(e);
        }
        return user;
    }


    public User updateUsers(User user) {
        try {
            validation(user);
            if (users.containsKey(user.getId())) {
                if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                users.replace(user.getId(), user);
            } else {
                throw new ValidationException("Такого пользователя не существует");
            }
        } catch (RuntimeException e) {
            throw new ValidationException(e);
        }
        return user;
    }

    private void validation(User user) throws ValidationException {
        if (user.getBirthday().isAfter(NOW_DATE)) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }

    public User getUserById(Integer id ){
        return users.get(id);
    }
}
