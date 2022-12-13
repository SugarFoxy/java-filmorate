package ru.yandex.practicum.filmorate.utils.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewUtils {
    private final JdbcTemplate jdbcTemplate;
    public SqlRowSet getReviewRowSetByReviewId(int id) {
        String sqlQuery = "SELECT R.*, FR.FILM_ID, UR.USER_ID FROM REVIEW_MODEL R\n" +
                "INNER JOIN FILM_REVIEWS FR on R.REVIEW_ID = FR.REVIEW_ID\n" +
                "INNER JOIN USER_REVIEWS UR on R.REVIEW_ID = UR.REVIEW_ID " +
                "WHERE R.REVIEW_ID=?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id);
    }
}
