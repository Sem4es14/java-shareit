package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.dto.requestDto.UserUpdateRequest;
import ru.practicum.shareit.user.dto.responseDto.UserResponse;
import ru.practicum.shareit.user.exception.UserAlreadyExists;
import ru.practicum.shareit.user.exception.UserNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExists("User with email: " + request.getEmail() + " is already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();

        return UserMapper.fromUserToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(UserUpdateRequest request, Long id) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExists("User with email: " + request.getEmail() + " is already exists");
        }
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFound("User with id: " + id + " is not found");
        });
        user.setEmail(request.getEmail() == null ? user.getEmail() : request.getEmail());
        user.setName(request.getName() == null ? user.getName() : request.getName());

        return UserMapper.fromUserToResponse(user);
    }

    @Override
    public UserResponse get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFound("User with id: " + id + " is not found");
        });

        return UserMapper.fromUserToResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return UserMapper.fromUsersToResponses(userRepository.getAll());
    }

    @Override
    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFound("User with id: " + id + " is not found");
        });
        userRepository.delete(id);
    }
}
