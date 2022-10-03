package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class User {
    int id;
    @NonNull
    String email;
    @NonNull
    String login;
    String name;
    LocalDate birthday;

}
