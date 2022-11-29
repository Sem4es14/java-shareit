package ru.practicum.shareit.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.exception.dto.ExceptionDTO;
import ru.practicum.shareit.exception.model.AlreadyExistsException;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ItemExceptionHandler {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionDTO> notFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ExceptionDTO> forbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> alreadyExistsException(AlreadyExistsException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<ExceptionDTO> missingRequestHeaderException(MissingRequestHeaderException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ExceptionDTO(
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ExceptionDTO> validationException(ValidationException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(new ExceptionDTO("Unknown state: UNSUPPORTED_STATUS", LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }
}
