package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Film {

    private int id;
    @NotBlank(message = "Название отсутствует")
    private String name;
    @NotNull(message = "Филь не может быть без описания")
    @Size(max = 200, message = "Максимальное коллычество смволов - 200")
    private String description;
    @NotNull(message = "Дата не должна быть пустой")
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность не может отсутствовать")
    @Positive(message = "Продолжитьльность не может быть отрицательной")
    private long duration;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}