package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.ExceptionDTO;

import java.time.LocalDateTime;

@ControllerAdvice
public class ItemExceptionHandler {
    @ExceptionHandler(value = ItemNotFound.class)
    public ResponseEntity<ExceptionDTO> itemNotFound(ItemNotFound e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccessToItemForbidden.class)
    public ResponseEntity<ExceptionDTO> accessToItemForbidden(AccessToItemForbidden e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<ExceptionDTO> missingRequestHeaderException(MissingRequestHeaderException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ExceptionDTO> runtimeException(RuntimeException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
