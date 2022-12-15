package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FilmReviewDto {
    @Id
    private int reviewId;
    @NotBlank(message = "отзыв не может быть пустым")
    private String content;
    @NotNull(message = "отзыв должен быть либо положительным, либо отрицательным")
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;
    @NotNull(message = "кто-то всё-таки должен оставить отзыв")
    private Integer userId;
    @NotNull(message = "отзыв должен быть оставлен на фильм")
    private Integer filmId;
    private Integer useful = 0;

    public FilmReviewDto(String content, Boolean isPositive, Integer userId, Integer filmId, Integer useful) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }

    public FilmReviewDto(int reviewId, String content, Boolean isPositive, Integer userId, Integer filmId, Integer useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }
}
