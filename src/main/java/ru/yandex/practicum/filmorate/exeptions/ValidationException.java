package ru.yandex.practicum.filmorate.exeptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Ошибка валидации")
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
        log.info("Ошибка валидации: {}", message);
    }

    public String toJson() {
        return "{}";
    }
}
