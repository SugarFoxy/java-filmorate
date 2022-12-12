package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public abstract class Review {


    public Review(int id, String content, Boolean isPositive, Integer useful) {
        this.id = id;
        this.content = content;
        this.isPositive = isPositive;
        this.useful = useful;
    }

    private int id;
    @NotBlank(message = "отзыв не может быть пустым")
    private String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    private Boolean isPositive;
    private Integer useful = 0;

}
