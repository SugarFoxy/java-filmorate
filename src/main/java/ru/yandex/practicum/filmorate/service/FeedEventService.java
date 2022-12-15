package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.FeedEventStorage;
import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.util.List;

@Service
@Slf4j
public class FeedEventService {
    private final FeedEventStorage eventStorage;

    @Autowired
    public FeedEventService(FeedEventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    @EventListener(FeedEvent.class)
    public void onEvent(FeedEvent event) {
        eventStorage.save(event);
    }

    public List<FeedEvent> getFeed(int userId) {
        return eventStorage.getByUserId(userId);
    }
}