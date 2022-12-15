package ru.yandex.practicum.filmorate.storages;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmReviewStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private ReviewsStorage reviewsStorage;

    private Film film;
    private User user;
    private FilmReview review;


    @BeforeEach
    public void beforeEach() {
        user = new User("erewd@gmail.com", "eUsweer", "Mulenaewsss", LocalDate.of(1995, 7, 11));
        film = new Film("Название фильма", "Описание фильма",
                LocalDate.of(2000, 10, 10), 100L, new Mpa(1, null, null));
        review = new FilmReview(1, "content", true, 0, user, film);
        userStorage.addUser(user);
        filmStorage.addFilm(film);
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("Checks add and get methods")
    public void addReview() {
        reviewsStorage.addReview(review);
        FilmReview testReview = reviewsStorage.getReview(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(testReview.getContent(), review.getContent()),
                () -> Assertions.assertEquals(testReview.getFilm().getName(), review.getFilm().getName()),
                () -> Assertions.assertEquals(testReview.getUser().getName(), review.getUser().getName()),
                () -> Assertions.assertEquals(testReview.getIsPositive(), review.getIsPositive())
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("Checks add and get methods")
    public void deleteReview() {
        reviewsStorage.addReview(review);
        reviewsStorage.deleteReview(1);
        String sqlQuery = "SELECT * FROM REVIEW_MODEL WHERE REVIEW_ID=1";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery);
        assertFalse(userRow.next());
        assertThrows(EntityNotFoundException.class, () -> reviewsStorage.deleteReview(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests update method")
    public void updateReview() {
        FilmReview newReview = new FilmReview(1, "newContent", false, 0, user, film);
        reviewsStorage.addReview(review);
        newReview.setId(1);
        reviewsStorage.updateReview(newReview);
        FilmReview testReview = reviewsStorage.getReview(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(testReview.getContent(), newReview.getContent()),
                () -> Assertions.assertEquals(testReview.getFilm().getName(), newReview.getFilm().getName()),
                () -> Assertions.assertEquals(testReview.getUser().getName(), newReview.getUser().getName()),
                () -> Assertions.assertEquals(testReview.getIsPositive(), newReview.getIsPositive())
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests getReview with film id method")
    public void getFilmReviewByFilmId() {
        Film testFilm = filmStorage.getFilmById(1);
        review.setFilm(testFilm);
        reviewsStorage.addReview(review);
        Set<FilmReview> testReviews = reviewsStorage.getFilmReviews(testFilm.getId(), 10);
        Assertions.assertAll(
                () -> Assertions.assertTrue(testReviews.contains(review)),
                () -> Assertions.assertEquals(review.getFilm().getName(), testFilm.getName())
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests getAllReviews method")
    public void getAllFilmReviews() {
        User newUser = new User("eee@gmail.com", "login login", "Mulenias",
                LocalDate.of(1995, 7, 11));
        Film newFilm = new Film("Название фильма новое ", "Описание фильма новое",
                LocalDate.of(2000, 10, 10), 100L, new Mpa(1, null, null));
        userStorage.addUser(newUser);
        filmStorage.addFilm(newFilm);
        FilmReview newReview = new FilmReview(2, "AddContent", false, 0, newUser, newFilm);
        reviewsStorage.addReview(review);
        reviewsStorage.addReview(newReview);
        FilmReview getReview = reviewsStorage.getReview(1);
        FilmReview getSecondReview = reviewsStorage.getReview(1);

        Set<FilmReview> reviews = reviewsStorage.getReviews(10);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, reviews.size()),
                () -> Assertions.assertTrue(reviews.contains(getReview)),
                () -> Assertions.assertTrue(reviews.contains(getSecondReview))
        );
    }
}
