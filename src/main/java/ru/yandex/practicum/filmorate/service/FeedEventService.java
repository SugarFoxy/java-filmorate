package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.model.FeedEvent;

import java.util.List;

@Service
@Slf4j
public class FeedEventService {
    private final EventStorage eventStorage;
    private ObjectMapper objectMapper;

    @Autowired
    public FeedEventService(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @EventListener(FeedEvent.class)
    public void onEvent(FeedEvent event) {
        eventStorage.save(event);

        trace(event);
    }

    public List<FeedEvent> getFeed(int userId) {
        return eventStorage.getByUserId(userId);
    }

    private void trace(FeedEvent event) {
        if (objectMapper != null && log.isTraceEnabled()) {
            try {
                log.trace("{}: {}", event.getClass(), objectMapper.writeValueAsString(event));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}