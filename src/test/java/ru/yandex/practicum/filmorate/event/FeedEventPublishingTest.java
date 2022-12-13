package ru.yandex.practicum.filmorate.event;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewsStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.model.FilmReview;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FeedEventPublishingTest {
    @Test
    public void shouldPublishFeedEventWhenAddingAndDeletingFriend() {
        FriendsStorage friendStorage = mock(FriendsStorage.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        UserService userService = new UserService(
                mock(UserStorage.class), friendStorage, mock(FilmStorage.class), publisher);
        ArgumentCaptor<FeedEvent> eventCaptor = ArgumentCaptor.forClass(FeedEvent.class);

        when(friendStorage.addToFriends(1, 2)).thenReturn(100);
        when(friendStorage.removeFromFriends(3, 4)).thenReturn(200);
        doNothing().when(publisher).publishEvent(eventCaptor.capture());

        userService.addFriend(1, 2);

        assertEquals(100, eventCaptor.getValue().getEntityId());

        userService.removeFriend(3, 4);

        assertEquals(200, eventCaptor.getValue().getEntityId());
    }

    @Test
    public void shouldPublishFeedEventWhenAddingOrDeletingLike() {
        LikesStorage likesStorage = mock(LikesStorage.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        FilmService filmService = new FilmService(
                mock(FilmStorage.class), likesStorage, mock(GenreStorage.class), publisher);
        ArgumentCaptor<FeedEvent> eventCaptor = ArgumentCaptor.forClass(FeedEvent.class);

        when(likesStorage.addLikeToFilm(1, 2)).thenReturn(100);
        when(likesStorage.removeLikeFromFilm(3, 4)).thenReturn(200);
        doNothing().when(publisher).publishEvent(eventCaptor.capture());

        filmService.addLikeToFilm(1, 2);

        assertEquals(100, eventCaptor.getValue().getEntityId());

        filmService.removeLikeFromFilm(3, 4);

        assertEquals(200, eventCaptor.getValue().getEntityId());
    }

    @Test
    public void shouldPublishFeedEventWhenAddingOrUpdatingOrDeletingReview() {
        ReviewsStorage reviewStorage = mock(ReviewsStorage.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        ReviewService reviewService = new ReviewService(reviewStorage, publisher);
        User user = new User("user@test.tst", "user", "name", LocalDate.now());
        FilmReview review = new FilmReview(100, "content", true, 0, user, null);
        ArgumentCaptor<FeedEvent> eventCaptor = ArgumentCaptor.forClass(FeedEvent.class);

        user.setId(1);
        doNothing().when(publisher).publishEvent(eventCaptor.capture());

        when(reviewStorage.addReview(review)).thenReturn(review);
        reviewService.addReview(review);

        assertEquals(100, eventCaptor.getValue().getEntityId());

        when(reviewStorage.getReview(200)).thenReturn(review);
        review.setId(200);
        reviewService.updateReview(review);

        assertEquals(200, eventCaptor.getValue().getEntityId());

        review.setId(300);
        when(reviewStorage.getReview(300)).thenReturn(review);
        doNothing().when(reviewStorage).deleteReview(300);
        reviewService.deleteReview(300);

        assertEquals(300, eventCaptor.getValue().getEntityId());
    }
}