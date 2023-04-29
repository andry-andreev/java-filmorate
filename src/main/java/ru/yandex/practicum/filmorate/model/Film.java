package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.FilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Film {

    private int id;
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 0, max = 200)
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FilmReleaseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
