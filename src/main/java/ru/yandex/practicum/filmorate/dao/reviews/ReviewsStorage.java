package ru.yandex.practicum.filmorate.dao.reviews;

import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Set;

public interface ReviewsStorage {

    FilmReview addReview(FilmReview review);
    FilmReview updateReview(FilmReview review);
    Set<FilmReview> getReviews();
    FilmReview getReview(int id);
    Set<FilmReview> getFilmReviews(int id);

}
