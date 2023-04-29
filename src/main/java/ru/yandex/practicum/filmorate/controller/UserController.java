package ru.yandex.practicum.filmorate.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static Map<Integer, User> users = new HashMap<>();
    private int currentUserId = 0;

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        validateUser(user);
        setUserId(user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
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
