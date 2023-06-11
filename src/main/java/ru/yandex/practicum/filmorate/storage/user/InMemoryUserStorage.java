package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static Map<Integer, User> users = new HashMap<>();
    private int currentUserId = 0;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        setUserId(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();
        if (users.containsKey(id)) {
            user.setId(id);
            validateUser(user);
            users.put(id, user);
            return user;
        } else {
            throw new NotFoundException("", id);
        }
    }

    @Override
    public boolean removeUser(int id) {
        if (users.containsKey(id)) {
            users.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    private int getNextUserId() {
        return ++currentUserId;
    }

    public void setUserId(User user) {
        user.setId(getNextUserId());
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
