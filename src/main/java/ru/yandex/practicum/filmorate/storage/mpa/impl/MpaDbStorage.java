package ru.yandex.practicum.filmorate.storage.mpa.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAll() {
        String sql = "select * from RATING";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public MPA getById(Integer id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from RATING where RATING_ID = ?", id);
        if (mpaRows.next()) {
            return new MPA(mpaRows.getInt("rating_id"), mpaRows.getString("rating"));
        } else {
            throw new AbsenceOfObjectException("такого mpa-рейтинга не существует");
        }
    }

    private MPA makeMPA(ResultSet rs) throws SQLException {
        return new MPA(rs.getInt("rating_id"), rs.getString("rating"));
    }
}

