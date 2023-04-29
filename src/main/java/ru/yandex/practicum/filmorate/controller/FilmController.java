package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        setFilmId(film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        int id = film.getId();
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            return film;
        } else {
            throw new NotFoundException("", id);
        }
    }

    private int getNextFilmId() {
        return ++currentFilmId;
    }

    public void setFilmId(Film film) {
        film.setId(getNextFilmId());
    }
}