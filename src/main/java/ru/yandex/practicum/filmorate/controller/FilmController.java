package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static Map<Integer, Film> films = new HashMap<>();
    private int currentFilmId = 0;

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        try {
            validateFilm(film);
            setFilmId(film);
            films.put(film.getId(), film);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(film);
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.toJson());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        try {
            int id = film.getId();
            if (films.containsKey(id)) {
                film.setId(id);
                validateFilm(film);
                films.put(id, film);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(film);
            } else return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{}");
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    private int getNextFilmId() {
        return ++currentFilmId;
    }

    public void setFilmId(Film film) {
        film.setId(getNextFilmId());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}