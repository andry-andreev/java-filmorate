package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {

    private Integer id;
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
