package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User {
    private int id;
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
    private List<User> friends;
    @AssertFalse(message = "Логин не может содержать пробелы")
    private boolean loginHaveSpace; //***NOT isConditionTrue***

    private boolean isLoginHaveSpace() {
        return login.contains(" ");
    }
}
