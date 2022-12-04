package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewDto {

    public ReviewDto(String content, boolean isPositive, int rate, int userId, int filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.rate = rate;
        this.userId = userId;
        this.filmId = filmId;
    }

    public ReviewDto(int id, String content, boolean isPositive) {
        this.id = id;
        this.content = content;
        this.isPositive = isPositive;
    }

    public ReviewDto() {
    }

    private int id;
    @NotBlank(message = "отзыв не может быть пустым")
    private String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    private boolean isPositive;
    private int rate;
    @NotBlank(message = "кто-то всё-таки должен оставить отзыв")
    private int userId;
    @NotBlank(message = "отзыв должен быть оставлен на фильм")
    private int filmId;
}
