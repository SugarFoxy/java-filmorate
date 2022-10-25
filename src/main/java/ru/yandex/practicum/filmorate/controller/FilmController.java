package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на список фильмов");
        return service.getAllFilms();
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

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handle(final MethodArgumentNotValidException e) {
        String[] allErrors = e.getAllErrors().toString().split(";");
        String massage = allErrors[allErrors.length-1];
        Map<String,String> map = Map.of("error", massage);
        log.warn(massage);
        return map;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handle(final AbsenceOfObjectException e) {
        Map<String,String> map = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return map;
    }
}

