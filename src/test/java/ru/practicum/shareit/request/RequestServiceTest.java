package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.dto.requestDto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.responseDto.ItemRequestResponse;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.dto.responseDto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    @Autowired
    private final ItemRequestService itemRequestService;
    @Autowired
    private final UserService userService;
    private UserResponse userResponse;
    private ItemRequestResponse requestResponse;
    private Long userId;

    @BeforeEach
    private void beforeEach() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("ivan", "mailing@mail.com");
        userResponse = userService.create(userCreateRequest);
        userCreateRequest.setEmail("maill@mail.com");
        userId = userService.create(userCreateRequest).getId();
        ItemRequestCreate itemRequestCreate = new ItemRequestCreate("i need a book");
        requestResponse = itemRequestService.createRequest(itemRequestCreate, userResponse.getId());
    }

    @Test
    public void getByRequester() {
        assertTrue(itemRequestService.getByRequester(userResponse.getId()).size() > 0);
    }

    @Test
    public void getByFailRequester() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getByRequester(99L));
    }

    @Test
    public void getAll() {
        assertTrue(itemRequestService.getAll(userId, 0L, 10).size() > 0);
    }

    @Test
    public void getAllFailRequester() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getAll(99L, 0L, 10));
    }

    @Test
    public void getById() {
        assertEquals(itemRequestService.getById(requestResponse.getId(), userResponse.getId()).getId(), requestResponse.getId());
    }

    @Test
    public void getBYIdFailItemRequest() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getById(99L, requestResponse.getId()));
    }

    @Test
    public void getBYIdFailUser() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getById(requestResponse.getId(), 99L));
    }
}
