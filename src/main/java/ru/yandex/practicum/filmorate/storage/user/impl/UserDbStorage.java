package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.frends.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendStorage friendStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("friendDbStorage") FriendStorage friendStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendStorage = friendStorage;
    }

    @Override
    public List<User> getUser() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        int id = 0;
        try {
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String query = "INSERT INTO USERS (NAME, LOGIN,EMAIL,BIRTHDAY) VALUES (?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getLogin());
                statement.setString(3, user.getEmail());
                statement.setDate(4, Date.valueOf(user.getBirthday()));
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        user.setId(id);
        user.setFriends(friendStorage.getAllFriendByUser(id));
        return user;
    }

    @Override
    public void updateUser(User user) {
        int amountLines = jdbcTemplate.update("UPDATE USERS SET NAME=?, " +
                        "LOGIN = ?, " +
                        "EMAIL = ?, " +
                        "BIRTHDAY= ? " +
                        "WHERE USER_ID = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (amountLines == 0) {
            throw new AbsenceOfObjectException("Пользователь для изменения не найден");
        }
    }

    @Override
    public User getUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);
        if(userRows.next()) {
            return User.builder()
                    .id(userRows.getInt("user_id"))
                    .name(userRows.getString("name"))
                    .login(userRows.getString("login"))
                    .email(userRows.getString("email"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .friends(friendStorage.getAllFriendByUser(userRows.getInt("user_id")))
                    .build();
        }else {
            throw new AbsenceOfObjectException("такого пользователя нет");
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
