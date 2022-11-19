package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUser() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS (NAME, LOGIN,EMAIL,BIRTHDAY) " +
                        "VALUES (?, ?, ?, ?);",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
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
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);
        return User.builder()
                .id(filmRows.getInt("user_id"))
                .name(filmRows.getString("name"))
                .login(filmRows.getString("login"))
                .email(filmRows.getString("email"))
                .birthday(filmRows.getDate("birthday").toLocalDate())
                .build();
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
