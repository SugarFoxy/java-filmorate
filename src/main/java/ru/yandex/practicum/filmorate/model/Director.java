package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Director {
    @Id
    private int id;
    @NotBlank(message = "Имя режиссера не может быть пустым.")
    private String name;
}
