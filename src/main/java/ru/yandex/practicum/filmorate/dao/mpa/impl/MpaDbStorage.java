package ru.yandex.practicum.filmorate.dao.mpa.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.mpa.MpaMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM mpa_dictionary WHERE mpa_id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRow.next()) {
            return MpaMapper.mapMpa(mpaRow);
        } else {
            throw new EntityNotFoundException("MPA с id " + id + " не найден.");
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> allMpa = new ArrayList<>();
        String sqlQuery = "SELECT * FROM mpa_dictionary";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery);
        while (mpaRow.next()) {
            allMpa.add(MpaMapper.mapMpa(mpaRow));
        }
        return allMpa;
    }
}
