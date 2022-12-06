package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public abstract class Review {

    public Review(int id, String content, boolean isPositive) {
        this.id = id;
        this.content = content;
        this.isPositive = isPositive;
    }

    private int id;
    @NotBlank(message = "отзыв не может быть пустым")
    private String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    private boolean isPositive;
    private int rate;

}
