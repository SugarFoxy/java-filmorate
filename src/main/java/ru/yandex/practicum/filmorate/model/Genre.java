package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Genre {
    @Id
    private final int id;
    private final String name;
    private final String description;
}
