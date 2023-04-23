package ru.yandex.practicum.filmorate.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity<String> addFilm(@Valid @RequestBody Film film) throws JsonProcessingException {
        ResponseEntity<String> response = filmManager.addFilm(film);
        return response;
    }

    @PutMapping
    public ResponseEntity<String> updateFilm(@Valid @RequestBody Film film) throws JsonProcessingException {
        ResponseEntity<String> response = filmManager.updateFilm(film.getId(), film);
        return response;
    }
}