package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage storage;

    @Autowired
    private UserService(InMemoryUserStorage storage){
        this.storage = storage;
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User postUser(User user) {
        if (user.getName()==null|| user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        return storage.addUser(user);
    }

    public void updateUsers(User user) throws AbsenceOfObjectException {
        storage.updateUsers(user);
    }

    public User getUserById(Integer id)throws AbsenceOfObjectException {
        if (storage.getMapUsers().containsKey(id)) {
            return storage.getUserById(id);
        }else {
            throw new AbsenceOfObjectException("Такого пользователя нет");
        }
    }

    public List<User> findAllFriends(Integer id){
        List<User> users = new ArrayList<>();
        for (Integer idet : storage.getUserById(id).getFriends()){
            users.add(storage.getUserById(idet));
        }
        return users;
    }

    public void addToFriends(Integer id, Integer otherId) throws AbsenceOfObjectException{
        if (!storage.getMapUsers().containsKey(id)
                || !storage.getMapUsers().containsKey(otherId)){
            throw new  AbsenceOfObjectException("Один или оба пользователя не были добавлены");
        }
        User you = storage.getUserById(id);
        User friend = storage.getUserById(otherId);
        you.addFriend(otherId);
        friend.addFriend(id);
    }


    public void removeFromFriends(Integer id, Integer otherId){
        User you = storage.getUserById(id);
        User friend = storage.getUserById(otherId);
        List<Integer> allFriends1 = new ArrayList<>(you.getFriends());
        allFriends1.remove(otherId);
        you.setFriends(allFriends1);
        List<Integer> allFriends2 = new ArrayList<>(friend.getFriends());
        allFriends2.remove(id);
        friend.setFriends(allFriends2);
    }

    public List<Integer> getMutualFriends(Integer id, Integer otherId){
        User you = storage.getUserById(id);
        User friend = storage.getUserById(otherId);
        if(you.getFriends() != null && friend.getFriends() != null){
        return you.getFriends().stream()
                .filter(u->friend.getFriends().contains(u))
                .collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }
    }

}
