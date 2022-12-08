package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.requestdto.UserCreateRequest;
import ru.practicum.shareit.user.dto.requestdto.UserUpdateRequest;
import ru.practicum.shareit.user.dto.responsedto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserCreateRequest request) {
        return ResponseEntity.of(Optional.of(userService.create(request)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.of(Optional.of(userService.update(request, userId)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> get(@PathVariable Long userId) {
        return ResponseEntity.of(Optional.of(userService.get(userId)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.of(Optional.of(userService.getAll()));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
