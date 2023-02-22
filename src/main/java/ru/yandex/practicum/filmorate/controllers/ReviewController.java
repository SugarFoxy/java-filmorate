package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewsService;
    private final FilmReviewDtoMapper mapper;

    @Autowired
    public ReviewController(ReviewService reviewsService, FilmReviewDtoMapper mapper) {
        this.reviewsService = reviewsService;
        this.mapper = mapper;
    }

    @PostMapping
    public FilmReviewDto postReview(@Valid @RequestBody FilmReviewDto dto) {
        FilmReview review = reviewsService.addReview(mapper.toFilmReview(dto));
        return mapper.toDto(review);
    }

    @PutMapping
    public FilmReviewDto putReview(@Valid @RequestBody FilmReviewDto dto) {
        FilmReview review = reviewsService.updateReview(mapper.toFilmReview(dto));
        return mapper.toDto(review);
    }

    @GetMapping("/{reviewId}")
    public FilmReviewDto getReviews(@PathVariable int reviewId) {
        return mapper.toDto(reviewsService.getReview(reviewId));
    }

    @GetMapping
    public Set<FilmReviewDto> getFilmReviews(@RequestParam Optional<Integer> filmId,
                                             @RequestParam(defaultValue = "10") int count) {
        return reviewsService.getFilmReviews(filmId, count)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable int reviewId) {
        reviewsService.deleteReview(reviewId);
        return "Отзыв удалён";
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public String addLikeToReview(@PathVariable int reviewId,
                                  @PathVariable int userId) {
        reviewsService.addLike(reviewId, userId);
        return "Лайк добавлен";
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public String addDislikeToReview(@PathVariable int reviewId,
                                     @PathVariable int userId) {
        reviewsService.removeLike(reviewId, userId);
        return "Дизлайк добавлен";
    }
}