package ru.yandex.practicum.filmorate.utils.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SqlRowSet getSqlRowSetByUserId(int id) {
        String sqlQuery = "SELECT * FROM users_model WHERE user_id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id);
    }
}
