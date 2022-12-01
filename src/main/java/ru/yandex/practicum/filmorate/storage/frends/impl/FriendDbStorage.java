package ru.yandex.practicum.filmorate.storage.frends.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.storage.frends.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        try {
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friend where USER_ID =? and FRIEND_ID = ?",userId,friendId);
            if (friendRows.next()) {
                jdbcTemplate.update("UPDATE FRIEND SET STATUS = true where id = ?", friendRows.getInt("id"));
            } else {
                jdbcTemplate.update("INSERT INTO FRIEND(user_id, friend_id, status) values ( ?,?,? );",
                        userId, friendId, true);
                jdbcTemplate.update("INSERT INTO FRIEND(user_id, friend_id, status) values ( ?,?,? );",
                        friendId, userId, false);
            }
        } catch (RuntimeException e1) {
            throw new AbsenceOfObjectException("пользователь не существует");
        }
    }


    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("UPDATE FRIEND SET STATUS = false where USER_ID = ? and FRIEND_ID = ?",
                userId, friendId);
    }

    @Override
    public List<Integer> getAllFriendByUser(int userId) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIEND WHERE USER_ID = ? and STATUS = true";
        try {
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createFriendId(rs), userId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private int createFriendId(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }
}
