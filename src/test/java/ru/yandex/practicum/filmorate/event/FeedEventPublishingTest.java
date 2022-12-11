package ru.yandex.practicum.filmorate.event;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

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
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        ArgumentCaptor<FeedEvent> eventCaptor = ArgumentCaptor.forClass(FeedEvent.class);
    }
}