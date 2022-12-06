package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.utils.review.FilmReviewDtoMapper;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewsService;
    private final FilmReviewDtoMapper mapper;


    @GetMapping
    public ResponseEntity<Set<FilmReviewDto>> getReviews() {
        return null;
    }

    @PostMapping
    public ResponseEntity<FilmReviewDto> postReview(@RequestBody FilmReviewDto dto) {
        FilmReview review = reviewsService.addReview(mapper.toFilmReview(dto));
        return ResponseEntity.ok(mapper.toDto(review));
    }

}
