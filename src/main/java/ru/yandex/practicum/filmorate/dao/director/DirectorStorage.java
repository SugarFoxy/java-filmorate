package ru.yandex.practicum.filmorate.dao.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director addDirector(Director director);

    Director updateDirector(Director director); // возвращается режиссер дла тестов постмана

    Director getDirectorById(int id);

    void deleteDirector(int id);

    List<Director> getAllDirectors();
}
