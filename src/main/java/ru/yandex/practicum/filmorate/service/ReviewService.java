package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewsStorage reviewsStorage;
    private final ApplicationEventPublisher publisher;

    public FilmReview addReview(FilmReview review) {
        reviewsStorage.addReview(review);
        publisher.publishEvent(
                new FeedEvent(review.getUser().getId(), EventType.REVIEW, Operation.ADD, review.getId()));
        return review;
    }

    public FilmReview updateReview(FilmReview review) {
        FilmReview reviewToBeUpdated = getReview(review.getId());
        reviewsStorage.updateReview(review);
        publisher.publishEvent(
                new FeedEvent(reviewToBeUpdated.getUser().getId(),
                        EventType.REVIEW, Operation.UPDATE, review.getId()));
        return review;
    }

    public FilmReview getReview(int id) {
        return reviewsStorage.getReview(id);
    }

    public Set<FilmReview> getFilmReviews(Optional<Integer> id, int count) {
        return id.map(integer -> reviewsStorage.getFilmReviews(integer, count)
                .stream()
                .sorted(Comparator.comparing(FilmReview::getUseful).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new))).orElseGet(() -> reviewsStorage.getReviews(count)
                .stream()
                .sorted(Comparator.comparing(FilmReview::getUseful).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public void deleteReview(int id) {
        FilmReview review = getReview(id);
        reviewsStorage.deleteReview(id);
        publisher.publishEvent(
                new FeedEvent(review.getUser().getId(), EventType.REVIEW, Operation.REMOVE, id));
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
