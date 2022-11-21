package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage storage;

    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public List<Genre> getAll() {
        log.info("Получен запрос на список жанров");
        return storage.getAll();
    }

    public Genre getById(Integer id) {
        log.info("Получен запрос на получение жанра по ID");
        return storage.getById(id);
    }
}
