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

    public Set<FilmReview> getReviews(int count) {
        return reviewsStorage.getReviews(count);
    }

    public FilmReview getReview(int id) {
        return reviewsStorage.getReview(id);
    }

    public Set<FilmReview> getFilmReviews(int id, int count) {
      return reviewsStorage.getFilmReviews(id, count);
    }

    public void deleteReview(int id) {
        reviewsStorage.deleteReview(id);
    }

    public void addLike(int reviewId, int userId) {
        FilmReview filmReview = reviewsStorage.getReview(reviewId);
        int rate = filmReview.getUseful();
        filmReview.setUseful(rate + 1);
        reviewsStorage.saveReviewRate(filmReview.getId(), filmReview.getUseful());
    }

    public void removeLike(int reviewId, int userId) {
        FilmReview filmReview = reviewsStorage.getReview(reviewId);
        int rate = filmReview.getUseful();
        filmReview.setUseful(rate - 1);
        reviewsStorage.saveReviewRate(filmReview.getId(), filmReview.getUseful());
    }
}
