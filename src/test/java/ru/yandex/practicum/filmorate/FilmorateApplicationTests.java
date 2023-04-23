package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmManager;
import ru.yandex.practicum.filmorate.service.UserManager;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    private FilmManager filmManager;
    private UserManager userManager;

    @Test
    void contextLoads() {
    }

    @Test
    void testEmptyFilmName() {
        Film film = new Film(0, "", "description",
                LocalDate.of(2000, 1, 1), 110);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testEmptyFilmDescription() {
        Film film = new Film(0, "Film", null,
                LocalDate.of(2000, 1, 1), 110);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testOversizeFilmDescription() {
        Film film = new Film(0, "Film", "cutyzwzhhsldeqjauaqeldcwsvblruxztaxdhulbpbqvsglhdolhhtvpcy" +
                "whagwhyuxkhvckflcthkdkhhoulmnqfvfnyfajjorutzgmqjgnhwzfyomdixabeiunjooynqdxqskrctnxvqmbdajvlyvkeapzh" +
                "dyyrefjuexrmnsfqfvjhijlnqeyjduillveinqdeoskg",
                LocalDate.of(2000, 1, 1), 110);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testFilmReleaseTimeInFuture() {
        Film film = new Film(0, "Film", "description",
                LocalDate.of(2077, 1, 1), 110);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testFilmReleaseTimeInToFarPast() {
        Film film = new Film(0, "Film", "description",
                LocalDate.of(1800, 1, 1), 110);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testFilmDurationIsPositive() {
        Film film = new Film(0, "Film", "description",
                LocalDate.of(2000, 1, 1), 0);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserEmailIsNotEmail() {
        User user = new User(0, "testmail.com", "testuserLogin", "testuser",
                LocalDate.of(2000, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserEmailIsNull() {
        User user = new User(0, null, "testuserLogin", "testuser",
                LocalDate.of(2000, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserEmail() {
        User user = new User(0, "testmail.com", "testuserLogin", "testuser",
                LocalDate.of(2000, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserLoginNull() {
        User user = new User(0, "test@mail.com", null, "testuser",
                LocalDate.of(2000, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserLoginWithSpaces() {
        User user = new User(0, "test@mail.com", "test user Login", "testuser",
                LocalDate.of(2000, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserNameIsNull() {
        User user = new User(1, "test@mail.com", "testuserLogin", null,
                LocalDate.of(2000, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        try {
            UserManager userManagerTest = new UserManager();
            Field usersField = UserManager.class.getDeclaredField("users");
            usersField.setAccessible(true);
            Map<Integer, User> users = (Map<Integer, User>) usersField.get(userManagerTest);
            assertEquals(users.get(user.getId()).getName(), "testuserLogin");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUserBirthdayInFuture() {
        User user = new User(0, "test@mail.com", "test user Login", "testuser",
                LocalDate.of(2077, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
