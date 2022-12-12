package ru.yandex.practicum.filmorate.dao.director.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.utils.director.DirectorMapper;
import ru.yandex.practicum.filmorate.utils.director.DirectorUtils;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorUtils directorUtils;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate, DirectorUtils directorUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorUtils = directorUtils;
    }

    @Override
    public Director addDirector(Director director) {
        String sqlQuery = "INSERT INTO directors_model(director_name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        log.info("Режиссер с именем " + director.getName() + " был добавлен.");
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        if(directorUtils.getSqlRowSetByDirectorId(director.getId()).next()) {
            String sqlQuery = "UPDATE directors_model SET director_name = ? WHERE director_id = ?";
            jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
            log.info("Режиссер с id " + director.getId() + " обновлен.");
            return director;
        } else {
            throw new EntityNotFoundException("Режиссер с id " + director.getId() + " не найден.");
        }
    }

    @Override
    public Director getDirectorById(int id) {
        String sqlQuery = "SELECT * FROM directors_model WHERE director_id = ?";
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRow.next()) {
            return DirectorMapper.mapDirector(directorRow);
        } else {
            throw new EntityNotFoundException("Режиссер с id " + id + " не найден.");
        }
    }

    @Override
    public void deleteDirector(int id) {
        if (directorUtils.getSqlRowSetByDirectorId(id).next()) {
            String sqlQuery = "DELETE FROM directors_model WHERE director_id = ?";
            jdbcTemplate.update(sqlQuery, id);
            log.info("Режиссер с id " + id + " удален");
        } else {
            throw new EntityNotFoundException("Режиссер с id " + id + " не найден.");
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        List<Director> allDirectors = new ArrayList<>();
        String sqlQuery = "SELECT * FROM directors_model";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery);
        while (directorRows.next()) {
            allDirectors.add(DirectorMapper.mapDirector(directorRows));
        }
        return allDirectors;
    }
}
