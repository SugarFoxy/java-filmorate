package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    private List<Integer> friends;
    @NotNull(message = "Email не может отсутствовать")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть действительной")
    private String email;
    @NotNull(message = "логин не может быть null")
    @NotBlank(message = "логин не может быть пустым")
    private String login;
    private String name;
    @NotNull(message = "День рождения отсутствует")
    @Past(message = "День рождения не должен быть в будущем")
    private LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday){
        this.id = id;
        this.email = email;
        this.login =login;
        this.name = name;
        this.birthday = birthday;
    }

    public List<Integer> getFriends() {
        createFriends();
        return friends;
    }

    public void addFriend(Integer id) {
        createFriends();
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        createFriends();
        friends.remove(id);
    }

    private void createFriends(){
        if (friends == null) {
            friends = new ArrayList<>();
        }
    }
}
