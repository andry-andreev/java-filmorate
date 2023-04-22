package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FilmManager {
    private static Map<Integer, Film> films = new HashMap<>();
    private int currentFilmId = 0;

    private int getNextFilmId() {
        return currentFilmId++;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public ResponseEntity<String> addFilm(Film film) {
        try {
            validateFilm(film);
            films.put(film.getId(), film);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<String> updateFilm(int id, Film film) {
        try {
            film.setId(id);
            validateFilm(film);
            films.put(id, film);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    public void setFilmId(Film film) {
        film.setId(getNextFilmId());
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null) {
            throw new ValidationException("Не указанно поле описания");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
