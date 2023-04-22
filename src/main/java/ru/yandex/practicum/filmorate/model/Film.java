package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Film {

    private int id;
    private String name;
    private String description;
    @NonNull
    @Past
    private LocalDate releaseDate;
    @NonNull
    private int duration;

}
