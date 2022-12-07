package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestCreate requestCreate,
                                         @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.createRequest(requestCreate, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getByRequester(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.getByRequester(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") @Min(value = 0) Long from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                         @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.getAll(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getById(requestId, userId);
    }
}
