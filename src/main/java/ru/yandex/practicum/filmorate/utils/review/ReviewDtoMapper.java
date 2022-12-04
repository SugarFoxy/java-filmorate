package ru.yandex.practicum.filmorate.utils.review;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

@Mapper
public interface ReviewDtoMapper {

    ReviewDtoMapper REVIEW_DTO_MAPPER = Mappers.getMapper(ReviewDtoMapper.class);
    Review toReview(ReviewDto dto);
    ReviewDto toDto(Review review);
}
