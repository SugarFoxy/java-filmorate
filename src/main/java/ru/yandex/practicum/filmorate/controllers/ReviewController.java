package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.utils.review.FilmReviewDtoMapper;

import javax.validation.Valid;
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

    @GetMapping
    public ResponseEntity<Set<FilmReviewDto>> getReviews() {
        Set<FilmReviewDto> reviews = reviewsService.getReviews()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(reviews);

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<FilmReviewDto> getReviews(@PathVariable int reviewId) {
        return ResponseEntity.ok(mapper.toDto(reviewsService.getReview(reviewId)));
    }

    @GetMapping
    public ResponseEntity<Set<FilmReviewDto>> getFilmReviews(@RequestParam int id) {
        Set<FilmReviewDto> reviews = reviewsService.getFilmReviews(id)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(reviews);
    }


}
