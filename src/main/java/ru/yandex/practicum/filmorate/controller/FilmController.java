package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmManager;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmManager filmManager;

    @GetMapping
    public List<Film> getAllFilms() {
        return filmManager.getAllFilms();
    }

    @PostMapping
    public ResponseEntity<String> addFilm(@Valid @RequestBody Film film) {
        filmManager.setFilmId(film);
        ResponseEntity<String> response = filmManager.addFilm(film);
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFilm(@PathVariable Integer id, @Valid @RequestBody Film film) {
        ResponseEntity<String> response = filmManager.updateFilm(id, film);
        return response;
    }
}