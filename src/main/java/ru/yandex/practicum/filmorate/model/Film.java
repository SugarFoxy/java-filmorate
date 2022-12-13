package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private final String name;
    @NotNull(message = "Описание фильма не может быть null.")
    @Size(max = 200, message = "Длина описание не может быть больше 200 символов.")
    private final String description;
    @NotNull(message = "Дата выхода фильма не может быть null.")
    @ReleaseDateValidation
    private final LocalDate releaseDate;
    @NotNull(message = "Длительность фильма не может быть null.")
    @Positive(message = "Длительность фильма не может быть отрицательной.")
    private final Long duration;
    @NotNull(message = "Рейтинг фильма не может быть null.")
    private final Mpa mpa;
    private Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
    private List<Director> directors = new ArrayList<>();
}

