package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static Map<Integer, Film> films = new HashMap<>();
    private int currentFilmId = 0;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        setFilmId(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            return film;
        } else {
            throw new NotFoundException("", id);
        }
    }

    @Override
    public boolean removeFilm(int id) {
        if (films.containsKey(id)) {
            films.remove(id);
            return true;
        } else {
            return false;
        }
    }

    private int getNextFilmId() {
        return ++currentFilmId;
    }

    private void setFilmId(Film film) {
        film.setId(getNextFilmId());
    }
}
