package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.event.FeedEventStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
public class FeedEventStorageTest {
    @Autowired
    private FeedEventStorage eventStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserStorage userStorage;

    @Test
    @Sql("classpath:data.sql")
    public void testGettingFeedWhenEmpty() {
        assertEquals(Collections.emptyList(), eventStorage.getByUserId(1));
    }

    @Test
    @Sql("classpath:data.sql")
    public void testSavingThenGetting() {
        User user = new User("test@test.tst", "test", "test", LocalDate.now());

        userStorage.addUser(user);
        eventStorage.save(new FeedEvent(user.getId(), EventType.LIKE, Operation.ADD, 100));

        List<FeedEvent> events = eventStorage.getByUserId(user.getId());

        assertEquals(1, events.size());

        FeedEvent event = events.get(0);

        assertTrue(event.getTimestamp() > 0);
        assertTrue(event.getEventId() > 0);
        assertEquals(user.getId(), event.getUserId());
        assertEquals(EventType.LIKE, event.getEventType());
        assertEquals(Operation.ADD, event.getOperation());
        assertEquals(100, event.getEntityId());
    }

    @Test
    @Sql("classpath:data.sql")
    public void testIdAscendingOrder() {
        User user = new User("test@test.tst", "test", "test", LocalDate.now());

        userStorage.addUser(user);

        FeedEvent event1 = new FeedEvent(user.getId(), EventType.FRIEND, Operation.REMOVE, 100);
        FeedEvent event2 = new FeedEvent(user.getId(), EventType.LIKE, Operation.ADD, 200);

        eventStorage.save(event2);
        eventStorage.save(event1);

        List<FeedEvent> events = eventStorage.getByUserId(user.getId());

        assertEquals(2, events.size());
        assertEquals(1, events.get(0).getEventId());
        assertEquals(2, events.get(1).getEventId());
    }

    @Test
    @Sql("classpath:data.sql")
    public void testGettingByDifferentUsers() {
        User user1 = new User("test1@test.tst", "test1", "test1", LocalDate.now());
        User user2 = new User("test2@test.tst", "test2", "test2", LocalDate.now());

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        FeedEvent event1 = new FeedEvent(user1.getId(), EventType.FRIEND, Operation.REMOVE, 1);
        FeedEvent event2 = new FeedEvent(user2.getId(), EventType.LIKE, Operation.ADD, 2);

        eventStorage.save(event1);
        eventStorage.save(event2);

        List<FeedEvent> events = eventStorage.getByUserId(user1.getId());

        assertEquals(1, events.size());
        assertEquals(1, events.get(0).getEntityId());

        events = eventStorage.getByUserId(user2.getId());

        assertEquals(1, events.size());
        assertEquals(2, events.get(0).getEntityId());
    }
}