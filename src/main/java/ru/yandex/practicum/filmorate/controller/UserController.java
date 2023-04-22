package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserManager;

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
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        userManager.setUserId(user);
        ResponseEntity<String> response = userManager.addUser(user);
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        ResponseEntity<String> response = userManager.updateUser(id, user);
        return response;
    }
}
