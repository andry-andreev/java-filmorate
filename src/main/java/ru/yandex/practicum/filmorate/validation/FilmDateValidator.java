package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmDateValidator implements ConstraintValidator<FilmReleaseDate, LocalDate> {
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(FilmReleaseDate filmReleaseDate) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null)
            return false;
        return !value.isBefore(minDate);
    }
}
