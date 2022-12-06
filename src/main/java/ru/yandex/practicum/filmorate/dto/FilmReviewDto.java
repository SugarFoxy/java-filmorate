package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FilmReviewDto {

    public FilmReviewDto(String content, boolean isPositive, int userId, int filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public FilmReviewDto(int reviewId, String content, boolean isPositive) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
    }

    public FilmReviewDto(int reviewId, String content, boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public FilmReviewDto() {
    }

    private int reviewId;
    @NotBlank(message = "отзыв не может быть пустым")
    private String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    @JsonProperty(value = "isPositive")
    private boolean isPositive;
    @NotBlank(message = "кто-то всё-таки должен оставить отзыв")
    private int userId;
    @NotBlank(message = "отзыв должен быть оставлен на фильм")
    private int filmId;
}
