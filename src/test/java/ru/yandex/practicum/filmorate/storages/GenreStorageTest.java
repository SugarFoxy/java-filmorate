package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
public class GenreStorageTest {

    @Autowired
    private GenreStorage genreStorage;

    @Test
    @Sql("classpath:data.sql")
    public void getGenreById() {
        assertEquals("Драма", genreStorage.getGenreById(2).getName());
        assertThrows(EntityNotFoundException.class, () -> genreStorage.getGenreById(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getAllGenres() {
        assertEquals(6, genreStorage.getAllGenres().size());
    }
}
