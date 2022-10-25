package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.ExceptionDTO;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(value = UserNotFound.class)
    public ResponseEntity<ExceptionDTO> userNotFound(UserNotFound e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserAlreadyExists.class)
    public ResponseEntity<ExceptionDTO> userAlreadyExists(UserAlreadyExists e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ExceptionDTO(
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }
}
