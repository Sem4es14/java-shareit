package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.dto.requestDto.UserUpdateRequest;
import ru.practicum.shareit.user.dto.responseDto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;
    private UserResponse response;
    @Autowired
    private UserService service;

    @BeforeEach
    public void setup() {
        userCreateRequest = new UserCreateRequest(
                "ivan",
                "ivan@gmail.com"
        );
        response = UserResponse.builder()
                .id(1L)
                .name("ivan")
                .email("ivan@Gmail.com")
                .build();
        userUpdateRequest = new UserUpdateRequest();
    }

    @Test
    public void createUser() {
        assertNotNull(service.create(userCreateRequest).getId());
    }

    @Test
    public void updateUser() {
        userCreateRequest.setEmail("ivanivani@gmail.ru");
        response = service.create(userCreateRequest);
        userUpdateRequest.setEmail(response.getEmail());
        userUpdateRequest.setName("pavel");
        assertEquals(service.update(userUpdateRequest, response.getId()).getName(), "pavel");
    }

    @Test
    public void updateUserWithNullEmail() {
        userCreateRequest.setEmail("ivanivani@gmail.ru");
        response = service.create(userCreateRequest);
        userUpdateRequest.setEmail(null);
        userUpdateRequest.setName("pavel");
        assertEquals(service.update(userUpdateRequest, response.getId()).getName(), "pavel");
    }

    @Test
    public void updateUserWithNullName() {
        userCreateRequest.setEmail("ivanivanii@gmail.ru");
        response = service.create(userCreateRequest);
        userUpdateRequest.setEmail("vanvan@mail.com");
        userUpdateRequest.setName(null);
        assertEquals(service.update(userUpdateRequest, response.getId()).getEmail(), "vanvan@mail.com");
    }

    @Test
    public void getById() {
        userCreateRequest.setEmail("vanya@mail.ru");
        response = service.create(userCreateRequest);
        assertNotNull(service.get(response.getId()));
    }

    @Test
    public void getAll() {
        userCreateRequest.setEmail("vanyapetrov@mail.ru");
        service.create(userCreateRequest);
        assertTrue(service.getAll().size() > 0);
    }

    @Test
    public void delete() {
        userCreateRequest.setEmail("vanyapetrov@gmail.ru");
        response = service.create(userCreateRequest);
        service.delete(response.getId());
        assertThrows(NotFoundException.class, () -> service.get(response.getId()));
    }

    @Test
    public void updateUserFailId() {
        userCreateRequest.setEmail("ivanivani@gmail.ru");
        response = service.create(userCreateRequest);
        userUpdateRequest.setEmail(response.getEmail());
        userUpdateRequest.setName("pavel");
        assertThrows(NotFoundException.class, () -> service.update(userUpdateRequest, 99L));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->  service.delete(99L));
    }
}
