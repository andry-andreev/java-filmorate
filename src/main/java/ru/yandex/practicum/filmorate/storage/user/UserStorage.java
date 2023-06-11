package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    boolean removeUser(int id);

    User getUserById(int id);
}
