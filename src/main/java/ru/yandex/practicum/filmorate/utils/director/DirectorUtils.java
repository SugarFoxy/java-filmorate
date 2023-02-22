package ru.yandex.practicum.filmorate.utils.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class DirectorUtils {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SqlRowSet getSqlRowSetByDirectorId(int id) {
        String sqlQuery = "SELECT * FROM directors_model WHERE director_id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id);
    }// Вынес его в отдельный утилитарный класс, потому что используется в разных частях проектра
}
