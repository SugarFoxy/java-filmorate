package ru.yandex.practicum.filmorate.dao.reviews.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.model.FilmReview;

import java.sql.PreparedStatement;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReviewsDbStorage implements ReviewsStorage<FilmReview> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public FilmReview addReview(FilmReview review) {
        String sqlReview = "INSERT INTO REVIEW_MODEL(content, is_positive) VALUES ( ?,? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlReview, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.isPositive());
            return stmt;
        }, keyHolder);
        review.setId(1);

        String sqlUserReviews = "INSERT INTO USER_REVIEWS(user_id, review_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlUserReviews, review.getUser().getId(), review.getId());

        String sqlFilReviews = "INSERT INTO FILM_REVIEWS(FILM_ID, REVIEW_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlFilReviews, review.getFilm().getId(),  review.getId());
        return review;
    }

    @Override
    public FilmReview updateReview(FilmReview review) {
        return null;
    }

    @Override
    public Set<FilmReview> getReviews() {
        return null;
    }

    @Override
    public FilmReview getReview(int id) {
        return null;
    }
}
