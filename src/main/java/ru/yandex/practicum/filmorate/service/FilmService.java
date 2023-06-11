package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class FilmService {

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;
    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    private Map<Integer, Set<User>> filmLikes = new HashMap<>();

    public void addFilm(Film film) {
        filmLikes.put(film.getId(), new HashSet<User>());
    }

    public void addLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        User user = inMemoryUserStorage.getUserById(userId);
        filmLikes.computeIfAbsent(film.getId(), k -> new HashSet<>()).add(user);
    }

    public boolean removeLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        User user = inMemoryUserStorage.getUserById(userId);
        if (film != null && user != null) {
            filmLikes.get(film.getId()).remove(user);
            return true;
        } else {
            return false;
        }
    }

    public List<Film> getTopFilms(int n) {
        List<Map.Entry<Integer, Set<User>>> entries = new ArrayList<>(filmLikes.entrySet());
        entries.sort((a, b) -> b.getValue().size() - a.getValue().size());
        List<Film> topFilms = new ArrayList<>();
        for (int i = 0; i < n && i < entries.size(); i++) {
            int filmId = entries.get(i).getKey();
            topFilms.add(inMemoryFilmStorage.getFilmById(filmId));
        }
        return topFilms;
    }
}
