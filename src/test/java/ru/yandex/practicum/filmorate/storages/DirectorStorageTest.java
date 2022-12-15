package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.utils.director.DirectorMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
public class DirectorStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorStorage directorStorage;
    private Director director;

    @Autowired
    public DirectorStorageTest(JdbcTemplate jdbcTemplate, DirectorStorage directorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorStorage = directorStorage;
    }

    @BeforeEach
    public void beforeEach() {
        director = new Director(1, "Имя Режиссера");
    }

    @Test
    @Sql("classpath:data.sql")
    public void addDirector() {
        int id = directorStorage.addDirector(director).getId();
        String sqlQuery = "SELECT * FROM directors_model WHERE director_id = ?";
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        directorRow.next();
        assertEquals(director, DirectorMapper.mapDirector(directorRow));
    }

    @Test
    @Sql("classpath:data.sql")
    public void updateDirector() {
        int id = directorStorage.addDirector(director).getId();
        Director newDirector = new Director(id, "Новое имя режиссера");
        directorStorage.updateDirector(newDirector);
        String sqlQuery = "SELECT * FROM directors_model WHERE director_id = ?";
        SqlRowSet directorRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        directorRow.next();
        assertEquals(newDirector, DirectorMapper.mapDirector(directorRow));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getDirectorById() {
        directorStorage.addDirector(director);
        assertEquals(1, directorStorage.getDirectorById(1).getId());
    }

    @Test
    @Sql("classpath:data.sql")
    public void deleteDirector() {
        directorStorage.addDirector(director);
        assertEquals(director, directorStorage.getDirectorById(1));
        directorStorage.deleteDirector(1);
        assertThrows(EntityNotFoundException.class, () -> directorStorage.getDirectorById(1));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getAllDirectors() {
        Director newDirector = new Director(2, "Второй режиссер");
        directorStorage.addDirector(director);
        directorStorage.addDirector(newDirector);
        assertEquals(2, directorStorage.getAllDirectors().size());
    }
}
