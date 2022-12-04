package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class Review {

    private final int id;
    @NotBlank(message = "отзыв не может быть пустым")
    private final String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    private final boolean isPositive;
    private int rate;

}
