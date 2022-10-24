package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    private UserService(InMemoryUserStorage storage){
        this.inMemoryUserStorage = storage;
    }

    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    public User postUser(User user) {
        if (user.getName()==null|| user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        return inMemoryUserStorage.addUser(user);
    }

    public void updateUsers(User user) throws ValidationException{
            inMemoryUserStorage.updateUsers(user);
    }

    public List<User> findAllFriends(Integer id){
        return inMemoryUserStorage.getUserById(id).getFriends();
    }

    public void addToFriends(Integer id, Integer otherId){
        User you = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(otherId);
        List<User> allFriends1 = new ArrayList<>(you.getFriends());
        allFriends1.add(friend);
        you.setFriends(allFriends1);
        List<User> allFriends2 = new ArrayList<>(friend.getFriends());
        allFriends2.add(you);
        friend.setFriends(allFriends2);
    }


    public void removeFromFriends(Integer id, Integer otherId){
        User you = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(otherId);
        List<User> allFriends1 = new ArrayList<>(you.getFriends());
        allFriends1.remove(friend);
        you.setFriends(allFriends1);
        List<User> allFriends2 = new ArrayList<>(friend.getFriends());
        allFriends2.remove(you);
        friend.setFriends(allFriends2);
    }

    public List<User> getMutualFriends(Integer id, Integer otherId){
        User you = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(otherId);
        return you.getFriends().stream()
                .filter(u->friend.getFriends().contains(u))
                .toList();
    }

}
