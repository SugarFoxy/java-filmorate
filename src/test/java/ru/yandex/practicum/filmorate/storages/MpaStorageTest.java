package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
public class MpaStorageTest {
    @Autowired
    private MpaStorage mpaStorage;

    @Test
    @Sql("classpath:data.sql")
    public void getMpaById() {
        assertEquals("PG", mpaStorage.getMpaById(2).getName());
        assertThrows(EntityNotFoundException.class, () -> mpaStorage.getMpaById(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getAllMpa() {
        assertEquals(5, mpaStorage.getAllMpa().size());
    }
}
