package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FilmReview {
    @NotNull(message = "кто-то всё-таки должен оставить отзыв")
    private User user;
    @NotNull(message = "отзыв должен быть оставлен на фильм")
    private Film film;
    private int id;
    @NotBlank(message = "отзыв не может быть пустым")
    private String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    private Boolean isPositive;
    private Integer useful;

    public FilmReview(int id, String content, boolean isPositive, int useful, User user, Film film) {
        this.id = id;
        this.content = content;
        this.isPositive = isPositive;
        this.useful = useful;
        this.user = user;
        this.film = film;
    }
}

