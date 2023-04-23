package ru.yandex.practicum.filmorate.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserManager;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserManager userManager;

    @GetMapping
    public List<User> getAllUsers() {
        return userManager.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) throws JsonProcessingException {
        ResponseEntity<String> response = userManager.addUser(user);
        return response;
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@Valid @RequestBody User user) throws JsonProcessingException {
        ResponseEntity<String> response = userManager.updateUser(user.getId(), user);
        return response;
    }
}
