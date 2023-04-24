package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        try {
            validateUser(user);
            setUserId(user);
            users.put(user.getId(), user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(user);
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.toJson());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        try {
            int id = user.getId();
            if (users.containsKey(id)) {
                user.setId(id);
                validateUser(user);
                users.put(id, user);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(user);
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

    private int getNextUserId() {
        return ++currentUserId;
    }

    public void setUserId(User user) {
        user.setId(getNextUserId());
    }

    private void validateUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
