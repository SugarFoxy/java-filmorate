package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FeedEvent {
    private int eventId;
    private long timestamp;
    private int userId;
    private EventType eventType;
    private Operation operation;
    private int entityId;

    public FeedEvent() {
    }

    public FeedEvent(int userId, EventType eventType, Operation operation, int entityId) {
        this.timestamp = System.currentTimeMillis();
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}