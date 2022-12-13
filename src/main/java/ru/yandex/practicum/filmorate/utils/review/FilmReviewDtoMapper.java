package ru.yandex.practicum.filmorate.utils.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmReviewDto;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

@Component
@RequiredArgsConstructor
public class FilmReviewDtoMapper {
    private final FilmService filmService;
    private final UserService userService;

    public FilmReviewDto toDto(FilmReview review) {
        return new FilmReviewDto(review.getId(),
                review.getContent(), review.getIsPositive(),
                review.getUser().getId(), review.getFilm().getId(), review.getUseful());
    }

    public FilmReview toFilmReview(FilmReviewDto dto) {
        return new FilmReview(dto.getReviewId(), dto.getContent(), dto.getIsPositive(),
                dto.getUseful(), userService.getUserById(dto.getUserId()),
                filmService.getFilmById(dto.getFilmId()));
    }


}
