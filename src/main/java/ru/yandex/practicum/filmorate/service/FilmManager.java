package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.SimpleDateFormat;
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
    private ObjectMapper mapper = new ObjectMapper();

    public FilmManager() {
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    private int getNextFilmId() {
        return ++currentFilmId;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public ResponseEntity<String> addFilm(Film film) throws JsonProcessingException {
        try {
            validateFilm(film);
            setFilmId(film);
            films.put(film.getId(), film);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(mapper.writeValueAsString(film));
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.toJson());
        }
    }

    public ResponseEntity<String> updateFilm(int id, Film film) throws JsonProcessingException {
        try {
            if (films.containsKey(id)) {
                film.setId(id);
                validateFilm(film);
                films.put(id, film);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(mapper.writeValueAsString(film));
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
