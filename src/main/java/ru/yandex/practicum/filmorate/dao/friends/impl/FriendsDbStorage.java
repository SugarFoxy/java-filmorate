package ru.yandex.practicum.filmorate.dao.friends.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.user.UserMapper;
import ru.yandex.practicum.filmorate.utils.user.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserUtils userUtils;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate, UserUtils userUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.userUtils = userUtils;
    }

    @Override
    public int addToFriends(int userId, int friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("friends_id")
                .withTableName("users_friends");
        int friendsId = jdbcInsert
                .executeAndReturnKey(Map.of("user_id", userId, "user_friend_id", friendId))
                .intValue();

        log.info("Пользователь с id " + userId + " добавил в друзья пользователя с id " + friendId + ".");

        return friendsId;
    }

    @Override
    public int removeFromFriends(int userId, int friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);

        int friendsId = 0;
        List<Integer> ids = jdbcTemplate.query(
                "SELECT friends_id FROM users_friends WHERE user_id = ? AND user_friend_id = ?",
                (rs, rowNum) -> rs.getInt("friends_id"),
                userId, friendId);
        if (ids.size() > 0) {
            friendsId = ids.get(0);
            String sqlQuery = "DELETE FROM users_friends WHERE user_id = ? AND user_friend_id = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        }

        log.info("Пользователь с id " + userId + " удалил из друзей пользователя с id " + friendId + ".");

        return friendsId;
    }

    @Override
    public List<User> getUserFriends(int id) {
        checkUserExistence(id);
        List<User> userFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users_model " +
                "WHERE user_id IN (SELECT user_friend_id FROM users_friends WHERE user_id = ?)";
        SqlRowSet userFriendsRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (userFriendsRow.next()) {
            userFriends.add(UserMapper.mapUser(userFriendsRow));
        }
        return userFriends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        checkUserExistence(userId);
        checkUserExistence(otherId);
        List<User> commonFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users_model " +
                "WHERE user_id IN (SELECT user_friend_id FROM users_friends WHERE user_id = ? " +
                "INTERSECT SELECT user_friend_id FROM users_friends WHERE user_id = ?)";
        SqlRowSet commonFriendsRow = jdbcTemplate.queryForRowSet(sqlQuery, userId, otherId);
        while (commonFriendsRow.next()) {
            commonFriends.add(UserMapper.mapUser(commonFriendsRow));
        }
        return commonFriends;
    }

    private void checkUserExistence(int id) {
        if (!userUtils.getSqlRowSetByUserId(id).next()) {
            throw new EntityNotFoundException("Пользователь с " + id + " не найден.");
        }
    }
}
