package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Mpa {
    @Id
    private final int id;
    private String name;
    private String description;
}
