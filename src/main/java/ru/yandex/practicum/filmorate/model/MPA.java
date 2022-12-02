package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
public class Mpa {
    private final int id;
    private String name;
    private String description;
}
