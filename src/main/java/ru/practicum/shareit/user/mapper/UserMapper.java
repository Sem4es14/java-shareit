package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.responsedto.UserResponse;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponse fromUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserResponse> fromUsersToResponses(List<User> users) {
        return users.stream()
                .map(UserMapper::fromUserToResponse)
                .collect(Collectors.toList());
    }
}
