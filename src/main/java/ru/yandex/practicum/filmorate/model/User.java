package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {

    private Integer id;
    @Email
    private String email;
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
