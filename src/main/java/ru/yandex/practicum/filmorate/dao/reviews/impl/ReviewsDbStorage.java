package ru.yandex.practicum.filmorate.dao.reviews.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.utils.review.ReviewMapper;
import ru.yandex.practicum.filmorate.utils.review.ReviewUtils;

import java.sql.PreparedStatement;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ReviewsDbStorage implements ReviewsStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewUtils reviewUtils;
    private final ReviewMapper mapper;

    @Override
    public FilmReview addReview(FilmReview review) {
        String sqlReview = "INSERT INTO REVIEW_MODEL(content, is_positive) VALUES ( ?,? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlReview, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            return stmt;
        }, keyHolder);
        review.setId(keyHolder.getKey().intValue());

        String sqlUserReviews = "INSERT INTO USER_REVIEWS(user_id, review_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlUserReviews, review.getUser().getId(), review.getId());

        String sqlFilReviews = "INSERT INTO FILM_REVIEWS(FILM_ID, REVIEW_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlFilReviews, review.getFilm().getId(), review.getId());
        log.info("отзыв создан");
        return review;
    }

    @Override
    public FilmReview updateReview(FilmReview review) {
        if (!reviewUtils.getReviewRowSetByReviewId(review.getId()).next()) {
            throw new EntityNotFoundException(String.format("отзыв с id %d не найден", review.getId()));
        }
        String sql = "UPDATE REVIEW_MODEL SET CONTENT=?, IS_POSITIVE=? WHERE REVIEW_ID=?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getId());
        log.info(String.format("отзыв с id %d обновлен", review.getId()));
        return review;
    }

    @Override
    public Set<FilmReview> getReviews() {
        Set<FilmReview> reviews = new TreeSet<>(Comparator.comparing(FilmReview::getUseful));
        String sql = "SELECT R.*, FR.FILM_ID, UR.USER_ID FROM REVIEW_MODEL R " +
                "INNER JOIN FILM_REVIEWS FR on R.REVIEW_ID = FR.REVIEW_ID " +
                "INNER JOIN USER_REVIEWS UR on R.REVIEW_ID = UR.REVIEW_ID ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            reviews.add(mapper.mapReview(sqlRowSet));
        }
        return reviews;
    }

    @Override
    public FilmReview getReview(int id) {
        SqlRowSet rowSet = reviewUtils.getReviewRowSetByReviewId(id);
        if (!rowSet.next()) {
            throw new EntityNotFoundException(String.format("отзыв c id %d не найден", id));
        }
        log.info("отзыв с id {} получен", id);
        return mapper.mapReview(rowSet);
    }

    public Set<FilmReview> getFilmReviews(int id) {
        String sql = "SELECT REVIEW_ID FROM FILM_REVIEWS WHERE FILM_ID =?";
        List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, id);
        if (ids.isEmpty()) {
            throw new EntityNotFoundException(String.format("отзывы на фильм с id %d не найден", id));
        }
        return ids.stream()
                .map((this::getReview))
                .collect(Collectors.toSet());
    }
}
