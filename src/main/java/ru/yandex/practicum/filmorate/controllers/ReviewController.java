package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewsService;


    @GetMapping
    public ResponseEntity<Set<ReviewDto>> getReviews() {
        return null;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> postReview(@RequestBody ReviewDto dto) {
        ReviewDto dto1 = dto;
        return ResponseEntity.ok(reviewsService.addReview(dto));
    }

}
