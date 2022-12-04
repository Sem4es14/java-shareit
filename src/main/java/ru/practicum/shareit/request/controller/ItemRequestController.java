package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.requestdto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.responsedto.ItemRequestResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestResponse> create(@RequestBody @Valid ItemRequestCreate requestCreate,
                                                      @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return ResponseEntity.of(Optional.of(itemRequestService.createRequest(requestCreate, requesterId)));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestResponse>> getByRequester(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return ResponseEntity.of(Optional.of(itemRequestService.getByRequester(requesterId)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestResponse>> getAll(@RequestParam(defaultValue = "0") @Min(value = 0) Long from,
                                                            @RequestParam(defaultValue = "10") @Positive Integer size,
                                                            @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return ResponseEntity.of(Optional.of(itemRequestService.getAll(requesterId, from, size)));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestResponse> getById(@PathVariable @Positive Long requestId,
                                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(itemRequestService.getById(requestId, userId)));
    }
}
