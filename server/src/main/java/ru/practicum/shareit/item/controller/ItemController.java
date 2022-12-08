package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.requestdto.CommentCreateRequest;
import ru.practicum.shareit.comment.dto.responsedto.CommentResponse;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.requestdto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestdto.ItemUpdateRequest;
import ru.practicum.shareit.item.dto.responsedto.ItemResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ItemResponse> create(@RequestBody ItemCreateRequest request,
                                               @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(itemService.create(request, ownerId)));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> update(@RequestBody ItemUpdateRequest request,
                                               @PathVariable Long itemId,
                                               @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(itemService.update(request, itemId, ownerId)));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getByOwner(@RequestParam(defaultValue = "0") Long from,
                                                         @RequestParam(defaultValue = "10") Integer size,
                                                         @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(itemService.getByOwner(ownerId, from, size)));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getById(@PathVariable Long itemId,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(itemService.getById(itemId, userId)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse>> getBySearch(@RequestParam(defaultValue = "0") Long from,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(value = "text") String searchBy) {
        return ResponseEntity.of(Optional.of(itemService.getBySearch(searchBy, from, size)));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentCreateRequest request,
                                                      @PathVariable Long itemId,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(commentService.create(request, itemId, userId)));
    }

}
