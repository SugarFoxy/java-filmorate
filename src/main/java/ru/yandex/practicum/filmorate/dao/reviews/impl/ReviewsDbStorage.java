package ru.yandex.practicum.filmorate.dao.reviews.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.utils.review.ReviewDtoMapper;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReviewsDbStorage implements ReviewsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review addReview(ReviewDto dto) {
        String sqlReview = "INSERT INTO REVIEW_MODEL(content, is_positive) VALUES ( ?,? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlReview, new String[]{"review_id"});
            stmt.setString(1, dto.getContent());
            stmt.setBoolean(2, dto.isPositive());
            return stmt;
        }, keyHolder);
        dto.setId(1);

        String sqlUserReviews = "INSERT INTO USER_REVIEWS(user_id, review_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlUserReviews, dto.getUserId(), dto.getId());

        String sqlFilReviews = "INSERT INTO FILM_REVIEWS(FILM_ID, REVIEW_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlFilReviews, dto.getFilmId(),  dto.getId());
        return ReviewDtoMapper.REVIEW_DTO_MAPPER.toReview(dto);
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public Set<Review> getReviews() {
        return null;
    }

    @Override
    public Review getReview(int id) {
        return null;
    }
}
