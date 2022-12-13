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
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FeedEventPublishingTest {
    @Test
    public void shouldPublishFeedEventWhenAddingAndDeletingFriend() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        UserService userService = new UserService(
                mock(UserStorage.class), mock(FriendsStorage.class), mock(FilmStorage.class), publisher);
        ArgumentCaptor<FeedEvent> eventCaptor = ArgumentCaptor.forClass(FeedEvent.class);

        doNothing().when(publisher).publishEvent(eventCaptor.capture());

        userService.addFriend(1, 2);

        FeedEvent event = eventCaptor.getValue();

        assertEquals(1, event.getUserId());
        assertEquals(2, event.getEntityId());
        assertEquals(EventType.FRIEND, event.getEventType());
        assertEquals(Operation.ADD, event.getOperation());

        userService.removeFriend(3, 4);

        event = eventCaptor.getValue();

        assertEquals(3, event.getUserId());
        assertEquals(4, event.getEntityId());
        assertEquals(EventType.FRIEND, event.getEventType());
        assertEquals(Operation.REMOVE, event.getOperation());
    }

    @Test
    public void shouldPublishFeedEventWhenAddingOrDeletingLike() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        FilmService filmService = new FilmService(
                mock(FilmStorage.class), mock(LikesStorage.class), mock(GenreStorage.class), publisher);
        ArgumentCaptor<FeedEvent> eventCaptor = ArgumentCaptor.forClass(FeedEvent.class);

        doNothing().when(publisher).publishEvent(eventCaptor.capture());

        filmService.addLikeToFilm(1, 2);

        FeedEvent event = eventCaptor.getValue();

        assertEquals(2, event.getUserId());
        assertEquals(1, event.getEntityId());
        assertEquals(EventType.LIKE, event.getEventType());
        assertEquals(Operation.ADD, event.getOperation());

        filmService.removeLikeFromFilm(3, 4);

        event = eventCaptor.getValue();

        assertEquals(4, event.getUserId());
        assertEquals(3, event.getEntityId());
        assertEquals(EventType.LIKE, event.getEventType());
        assertEquals(Operation.REMOVE, event.getOperation());
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