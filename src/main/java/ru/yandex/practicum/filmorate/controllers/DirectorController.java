package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director, BindingResult bindingResult) {
        directorService.logValidationErrors(bindingResult);
        return directorService.addDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director, BindingResult bindingResult) {
        directorService.logValidationErrors(bindingResult);
        return directorService.updateDirector(director);
    }

    @GetMapping("/{directorId}")
    public Director getDirectorById(@PathVariable int directorId) {
        return directorService.getDirectorById(directorId);
    }

    @DeleteMapping("/{directorId}")
    public void deleteDirectorById(@PathVariable int directorId) {
        directorService.deleteDirector(directorId);
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }
}
