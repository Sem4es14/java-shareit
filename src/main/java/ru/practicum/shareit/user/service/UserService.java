package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.dto.requestDto.UserUpdateRequest;
import ru.practicum.shareit.user.dto.responseDto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserCreateRequest request);

    UserResponse update(UserUpdateRequest request, Long id);

    UserResponse get(Long id);

    List<UserResponse> getAll();

    void delete(Long id);
}
