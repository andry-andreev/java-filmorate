package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserManager {
    private static Map<Integer, User> users = new HashMap<>();
    private int currentUserId = 0;
    private ObjectMapper mapper = new ObjectMapper();

    public UserManager() {
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    private int getNextUserId() {
        return ++currentUserId;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public ResponseEntity<String> addUser(User user) throws JsonProcessingException {
        try {
            validateUser(user);
            setUserId(user);
            users.put(user.getId(), user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(mapper.writeValueAsString(user));
        } catch (ValidationException e) {
            log.error("Ошибка валидации: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.toJson());
        }
    }

    public ResponseEntity<String> updateUser(int id, User user) throws JsonProcessingException {
        try {
            if (users.containsKey(id)) {
                user.setId(id);
                validateUser(user);
                users.put(id, user);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(mapper.writeValueAsString(user));
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

    public void setUserId(User user) {
        user.setId(getNextUserId());
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
