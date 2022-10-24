package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User {
    private int id;
    @NotNull(message = "Email не может быть null")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть действительной")
    private String email;
    @NotNull(message = "логин не может быть null")
    @NotBlank(message = "логин не может быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;
    private List<User> friends;
}
