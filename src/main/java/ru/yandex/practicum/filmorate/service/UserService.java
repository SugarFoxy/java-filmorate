package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class UserService {

    public List<User> findAllFriends(User user){
        return user.getFriends();
    }

}
