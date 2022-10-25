package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ResponseDto.ItemResponse;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemCreateRequest request,
                                               @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(itemService.create(request, ownerId)));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> update(@Valid @RequestBody ItemUpdateRequest request,
                                               @PathVariable Long itemId,
                                               @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(itemService.update(request, itemId, ownerId)));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(itemService.getByOwner(ownerId)));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getById(@PathVariable Long itemId) {
        return ResponseEntity.of(Optional.of(itemService.getById(itemId)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse>> getBySearch(@RequestParam(value = "text") String searchBy) {
        return ResponseEntity.of(Optional.of(itemService.getBySearch(searchBy)));
    }
}