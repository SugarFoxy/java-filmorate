package ru.yandex.practicum.filmorate.dao.reviews;

import ru.yandex.practicum.filmorate.model.FilmReview;

import java.util.Set;

public interface ReviewsStorage {

    FilmReview addReview(FilmReview review);

    FilmReview updateReview(FilmReview review);

    void deleteReview(int id);

    Set<FilmReview> getReviews(int count);

    FilmReview getReview(int id);

    Set<FilmReview> getFilmReviews(int id, int count);

    void saveReviewRate(int id, int rate);

}
