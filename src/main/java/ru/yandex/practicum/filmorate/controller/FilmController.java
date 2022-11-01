package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id){
       return service.getFilmById(id);
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Фильм добавлен");
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
            log.info("Фильм обнавлен");
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable Integer id, @PathVariable Integer userId){
        log.info("Фильму {} поставлен Лайк", id);
        service.like(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId){
        log.info("У фильма {} удален лайк", id);
        service.deleteLike(id,userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count){
        log.info("Получен запрос на список популярных фильмов");
        return service.getPopularFilms(count);
    }
}

