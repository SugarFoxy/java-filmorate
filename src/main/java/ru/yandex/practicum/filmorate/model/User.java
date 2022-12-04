package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Data
public class User {
    private int id;
    @Email(message = "Введен некорректный email.")
    private final String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы.")
    private final String login;
    private String name;
    @Past(message = "День рождения не может быть в будущем.")
    private final LocalDate birthday;
    private Set<Review> reviews = new TreeSet<>(Comparator.comparing(Review::getRate));

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
