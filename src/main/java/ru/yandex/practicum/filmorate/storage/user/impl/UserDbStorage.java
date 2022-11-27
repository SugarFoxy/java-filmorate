package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
    public List<User> getUsers() {
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
    public User updateUser(User user) {
        int amountLines = jdbcTemplate.update("UPDATE USERS SET NAME=?, " +
                        "LOGIN = ?, " +
                        "EMAIL = ?, " +
                        "BIRTHDAY= ? " +
                        "WHERE ID = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (amountLines == 0) {
            throw new AbsenceOfObjectException("Пользователь для изменения не найден");
        }
        try {
            User changedUser = jdbcTemplate.queryForObject("select * from USERS where ID = ?", new BeanPropertyRowMapper<>(User.class), user.getId());
            int userId = user.getId();
            assert changedUser != null;
            changedUser.setFriends(friendStorage.getAllFriendByUser(userId));
            return changedUser;
        } catch (EmptyResultDataAccessException e) {
            throw new AbsenceOfObjectException("Измененный пользователь не найден");
        }
    }

    @Override
    public User getUserById(Integer id) {
        try {
            User user = jdbcTemplate.queryForObject("select * from USERS where ID = ?", new BeanPropertyRowMapper<>(User.class), id);
            assert user != null;
            user.setFriends(friendStorage.getAllFriendByUser(id));
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new AbsenceOfObjectException("Такого пользователя не существует");
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(friendStorage.getAllFriendByUser(rs.getInt("id")))
                .build();
    }
}
