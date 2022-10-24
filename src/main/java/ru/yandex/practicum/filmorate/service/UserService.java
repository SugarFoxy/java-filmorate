package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    public List<User> findAllFriends(User user){
        return user.getFriends();
    }

    public void addToFriends(User user1, User user2){
        List<User> allFriends1 = new ArrayList<>(user1.getFriends());
        allFriends1.add(user2);
        user1.setFriends(allFriends1);
        List<User> allFriends2 = new ArrayList<>(user2.getFriends());
        allFriends2.add(user1);
        user2.setFriends(allFriends2);
    }


    public void removeFromFriends(User user1, User user2){
        List<User> allFriends1 = new ArrayList<>(user1.getFriends());
        allFriends1.remove(user2);
        user1.setFriends(allFriends1);
        List<User> allFriends2 = new ArrayList<>(user2.getFriends());
        allFriends2.remove(user1);
        user2.setFriends(allFriends2);
    }

    public List<User> getMutualFriends(User user1, User user2){
        return user1.getFriends().stream()
                .filter(u->user2.getFriends().contains(u))
                .toList();
    }

}
