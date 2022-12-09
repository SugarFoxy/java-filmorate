package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmReview extends Review {

    public FilmReview(int id, String content, boolean isPositive) {
        super(id, content, isPositive);
    }

    public FilmReview(int id, String content, boolean isPositive, int useful, User user, Film film) {
        super(id, content, isPositive,useful);
        this.user = user;
        this.film = film;
    }

    private User user;
    private Film film;

}
