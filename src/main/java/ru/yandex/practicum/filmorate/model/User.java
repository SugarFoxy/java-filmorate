package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class User {
    private int id;
    private List<Integer> friends = new ArrayList<>();
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

    public List<Integer>  getFriends(){
        if (friends==null) {friends = new ArrayList<>();}
        return friends;
    }

    public void addFriend(Integer id){
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }

}
