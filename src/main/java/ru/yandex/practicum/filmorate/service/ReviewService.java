package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.model.FilmReview;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewsStorage reviewsStorage;

    public FilmReview addReview(FilmReview review) {
        return reviewsStorage.addReview(review);

    }

    public FilmReview updateReview(FilmReview review) {
        return reviewsStorage.updateReview(review);
    }

    public Set<FilmReview> getReviews() {
        return reviewsStorage.getReviews();
    }

    public FilmReview getReview(int id) {
        return reviewsStorage.getReview(id);
    }

    public Set<FilmReview> getFilmReviews(int id) {
      return reviewsStorage.getFilmReviews(id);
    }

}
