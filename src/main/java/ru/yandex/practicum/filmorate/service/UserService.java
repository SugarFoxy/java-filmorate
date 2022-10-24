package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void updateUsers(User user) throws AbsenceOfObjectException {
            inMemoryUserStorage.updateUsers(user);
    }

    public User getUserById(Integer id)throws AbsenceOfObjectException {
        if (inMemoryUserStorage.getMapUsers().containsKey(id)) {
            return inMemoryUserStorage.getUserById(id);
        }else {
            throw new AbsenceOfObjectException("Такого пользователя нет");
        }
    }

    public List<User> findAllFriends(Integer id){
        List<User> users = new ArrayList<>();
        for (Integer idet : inMemoryUserStorage.getUserById(id).getFriends()){
            users.add(inMemoryUserStorage.getUserById(idet));
        }
        return users;
    }

    public void addToFriends(Integer id, Integer otherId) throws AbsenceOfObjectException{
        if (!inMemoryUserStorage.getMapUsers().containsKey(id)
                || !inMemoryUserStorage.getMapUsers().containsKey(otherId)){
            throw new  AbsenceOfObjectException("Один или оба пользователя не были добавлены");
        }
        User you = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(otherId);
        you.addFriend(otherId);
        friend.addFriend(id);
    }


    public void removeFromFriends(Integer id, Integer otherId){
        User you = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(otherId);
        List<Integer> allFriends1 = new ArrayList<>(you.getFriends());
        allFriends1.remove(otherId);
        you.setFriends(allFriends1);
        List<Integer> allFriends2 = new ArrayList<>(friend.getFriends());
        allFriends2.remove(id);
        friend.setFriends(allFriends2);
    }

    public List<Integer> getMutualFriends(Integer id, Integer otherId){
        User you = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(otherId);
        if(you.getFriends() != null && friend.getFriends() != null){
        return you.getFriends().stream()
                .filter(u->friend.getFriends().contains(u))
                .collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }
    }

}
