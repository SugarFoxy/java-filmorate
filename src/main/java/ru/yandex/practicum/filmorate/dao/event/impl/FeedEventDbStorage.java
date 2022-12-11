package ru.yandex.practicum.filmorate.dao.event.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.event.FeedEventStorage;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.FeedEvent;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeedEventDbStorage implements FeedEventStorage {
    private static final String SQL_GET_EVENT_TYPES = "SELECT * FROM event_types_dictionary";
    private static final String SQL_GET_OPERATIONS = "SELECT * FROM event_operations_dictionary";
    private static final String SQL_SAVE =
            "INSERT INTO users_feed (user_id, event_type, event_operation, entity_id, event_time) " +
                "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_GET_BY_USER =
            "SELECT f.event_id, f.event_time, f.user_id, f.entity_id, t.event_type, o.event_operation " +
                "FROM users_feed f, event_types_dictionary t, event_operations_dictionary o " +
                "WHERE " +
                    "t.event_type_id = f.event_type AND " +
                    "o.event_operation_id = f.event_operation AND " +
                    "f.user_id = ? " +
                "ORDER BY f.event_time DESC";

    private final JdbcTemplate jdbcTemplate;

    private final Map<EventType, Integer> eventTypes;
    private final Map<Operation, Integer> operations;

    @Autowired
    public FeedEventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.eventTypes = new HashMap<>();
        cacheEventTypes();
        this.operations = new HashMap<>();
        cacheOperations();
    }

    @Override
    public void save(FeedEvent event) {
        jdbcTemplate.update(SQL_SAVE, event.getUserId(),
                eventTypes.get(event.getEventType()), operations.get(event.getOperation()),
                event.getEntityId(), event.getTimestamp());
    }

    @Override
    public List<FeedEvent> getByUserId(int userId) {
        return jdbcTemplate.query(SQL_GET_BY_USER, FeedEventDbStorage::mapRow, userId);
    }

    private void cacheEventTypes() {
        jdbcTemplate.queryForList(SQL_GET_EVENT_TYPES).forEach(
                row -> eventTypes.put(
                        EventType.valueOf((String) row.get("EVENT_TYPE")),
                        (Integer) row.get("EVENT_TYPE_ID")));
    }

    private void cacheOperations() {
        jdbcTemplate.queryForList(SQL_GET_OPERATIONS).forEach(
                row -> operations.put(
                        Operation.valueOf((String) row.get("EVENT_OPERATION")),
                        (Integer) row.get("EVENT_OPERATION_ID")));
    }

    private static FeedEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        FeedEvent event = new FeedEvent();

        event.setEventId(rs.getInt("event_id"));
        event.setTimestamp(rs.getLong("event_time"));
        event.setUserId(rs.getInt("user_id"));
        event.setEntityId(rs.getInt("entity_id"));
        event.setEventType(EventType.valueOf(rs.getString("event_type")));
        event.setOperation(Operation.valueOf(rs.getString("event_operation")));

        return event;
    }
}