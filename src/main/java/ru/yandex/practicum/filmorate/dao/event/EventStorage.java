package ru.yandex.practicum.filmorate.dao.event;

import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.util.List;

public interface EventStorage {
    void save(FeedEvent event);

    List<FeedEvent> getByUserId(int userId);
}