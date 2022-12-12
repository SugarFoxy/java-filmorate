package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.utils.review.FilmReviewDtoMapper;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewsService;
    private final FilmReviewDtoMapper mapper;


    @PostMapping
    public ResponseEntity<FilmReviewDto> postReview(@Valid @RequestBody FilmReviewDto dto) {
        FilmReview review = reviewsService.addReview(mapper.toFilmReview(dto));
        return ResponseEntity.ok(mapper.toDto(review));
    }

    @PutMapping
    public ResponseEntity<FilmReviewDto> putReview(@Valid @RequestBody FilmReviewDto dto) {
        FilmReview review = reviewsService.updateReview(mapper.toFilmReview(dto));
        return ResponseEntity.ok(mapper.toDto(review));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<FilmReviewDto> getReviews(@PathVariable int reviewId) {
        return ResponseEntity.ok(mapper.toDto(reviewsService.getReview(reviewId)));
    }

    @GetMapping
    public ResponseEntity<Set<FilmReviewDto>> getFilmReviews(@RequestParam Optional<Integer> filmId,
                                                             @RequestParam(defaultValue = "10") int count) {
        return ResponseEntity.ok(reviewsService.getFilmReviews(filmId, count)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId) {
        reviewsService.deleteReview(reviewId);
        return ResponseEntity.ok("Отзыв удалён");
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public ResponseEntity<String> addLikeToReview(@PathVariable int reviewId,
                                                  @PathVariable int userId) {
        reviewsService.addLike(reviewId, userId);
        return ResponseEntity.ok("Лайк добавлен");
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public ResponseEntity<String> addDislikeToReview(@PathVariable int reviewId,
                                                     @PathVariable int userId) {
        reviewsService.removeLike(reviewId, userId);
        return ResponseEntity.ok("Дизлайк добавлен");
    }
}
