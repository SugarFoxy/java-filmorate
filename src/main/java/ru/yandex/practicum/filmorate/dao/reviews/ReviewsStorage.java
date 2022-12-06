package ru.yandex.practicum.filmorate.dao.reviews;

import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Set;

public interface ReviewsStorage <T extends Review> {

    T addReview(T t);
    T updateReview(T review);
    Set<T> getReviews();
    T getReview(int id);

}
