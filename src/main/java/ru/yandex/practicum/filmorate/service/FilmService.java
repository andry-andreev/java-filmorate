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
    /*Создайте FilmService, который будет отвечать за операции с фильмами,
     — добавление и удаление лайка, вывод 10 наиболее популярных фильмов
     по количеству лайков. Пусть пока каждый пользователь может поставить
     лайк фильму только один раз.

     В этом примере используется Map с именем filmLikes, где ключом является
      название фильма, а значением - множество пользователей, которые поставили лайк этому фильму.

Метод addLike добавляет лайк фильму от пользователя. Он использует метод computeIfAbsent, чтобы
 гарантировать наличие множества пользователей для данного фильма в карте filmLikes, и затем добавляет
  имя пользователя в это множество с помощью метода add.

Метод removeLike удаляет лайк фильма от пользователя. Он проверяет наличие записи для данного
 фильма в карте filmLikes с помощью метода containsKey, и если запись существует, то удаляет имя
  пользователя из множества с помощью метода remove.

Метод getTopFilms возвращает список из n наиболее популярных фильмов по количеству лайков. Он
 создает список записей карты filmLikes, сортирует его по убыванию количества лайков с помощью
  метода sort, и затем добавляет первые n фильмов в список topFilms, который возвращается в качестве результата.

Этот код является лишь примером и может быть доработан и изменен в соответствии с требованиями.*/
    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;
    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    private Map<Integer, Set<User>> filmLikes = new HashMap<>();

    public void addFilm(Film film) {
        filmLikes.put(film.getId(), new HashSet<User>());
    }

//    public void updateFilm(Film film) {
//        int id = film.getId();
//        Film filmToUpdate = inMemoryFilmStorage.getFilmById(id);
//        Set<User> likesToUpdate = filmLikes.get(filmToUpdate);
//        filmLikes.put(film.getId(), likesToUpdate);
//    }

    public void addLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        User user = inMemoryUserStorage.getUserById(userId);
        filmLikes.computeIfAbsent(film.getId(), k -> new HashSet<>()).add(user);
    }

    public boolean removeLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        User user = inMemoryUserStorage.getUserById(userId);
        if (filmLikes.containsKey(film) && filmLikes.containsValue(user)) {
            filmLikes.get(film).remove(user);
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
