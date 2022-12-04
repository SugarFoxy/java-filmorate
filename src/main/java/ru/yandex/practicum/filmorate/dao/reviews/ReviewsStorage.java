package ru.yandex.practicum.filmorate.dao.reviews;

import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Set;

public interface ReviewsStorage {

    Review addReview(ReviewDto review);
    Review updateReview(Review review);
    Set<Review> getReviews();
    Review getReview(int id);

}
