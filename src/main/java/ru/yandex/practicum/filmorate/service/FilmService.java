package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService {
    FilmStorage storage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> getAllFilms() {
        return storage.getFilms();
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film){
       return storage.updateFilms(film);
    }

    public void like ( Integer id,Integer userId){
        storage.getFilmById(id).addLike(userId);
    }

    public void deleteLike(Integer id,Integer userId){
        storage.getFilmById(id).deleteLike(userId);
    }

}
