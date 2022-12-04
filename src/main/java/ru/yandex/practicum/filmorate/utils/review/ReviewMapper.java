package ru.yandex.practicum.filmorate.utils.review;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Review;

@UtilityClass
public class ReviewMapper {

    public static Review mapReview(SqlRowSet sqlRowSet) {
        return new Review(sqlRowSet.getInt("review_id"),
                sqlRowSet.getString("content"),
                sqlRowSet.getBoolean("is_positive"));
    }
}
