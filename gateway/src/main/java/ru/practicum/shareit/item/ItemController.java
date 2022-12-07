package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemCreateRequest request,
                                         @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.create(request, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Valid @RequestBody ItemUpdateRequest request,
                                         @PathVariable Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.update(request, itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwner(@RequestParam(defaultValue = "0") @Min(value = 0) Long from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.getByOwner(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getBySearch(@RequestParam(defaultValue = "0") @Min(value = 0) Long from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size,
                                              @RequestParam(value = "text") String searchBy,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getBySearch(searchBy, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentCreateRequest request,
                                             @PathVariable Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.addComment(request, itemId, userId);
    }

}
