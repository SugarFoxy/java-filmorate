package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewsStorage reviewsStorage;

    public ReviewDto addReview(ReviewDto dto) {
       reviewsStorage.addReview(dto);
        return dto;
    }

    public Review updateReview(Review review) {
        return reviewsStorage.updateReview(review);
    }

    public Review getReview(int id) {
        return reviewsStorage.getReview(id);
    }

//    public Set<ReviewDto> getReviews() {
//
//        return reviewsStorage.getReviews()
//                .stream()
//                .map();
//    }
}
