package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;
import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final UserService userService;
    private ItemCreateRequest itemCreateRequest;
    private ItemUpdateRequest itemUpdateRequest;
    private ItemResponse itemResponse;
    private Long userId;

    @BeforeEach
    public void beforeEach() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("ivan", "vanish@mail.com");
        userId = userService.create(userCreateRequest).getId();
        itemCreateRequest = new ItemCreateRequest("book", "beautiful book", true, null);
        itemResponse = itemService.create(itemCreateRequest, userId);
    }

    @Test
    public void updateName() {
        itemUpdateRequest = new ItemUpdateRequest("knife", null, null);
        assertEquals(itemService.update(itemUpdateRequest, itemResponse.getId(), userId).getName(), "knife");
    }

    @Test
    public void updateDescription() {
        itemUpdateRequest = new ItemUpdateRequest(null, "cool book", null);
        assertEquals(itemService.update(itemUpdateRequest, itemResponse.getId(), userId).getDescription(), "cool book");
    }

    @Test
    public void updateAvailable() {
        itemUpdateRequest = new ItemUpdateRequest(null, null, false);
        assertEquals(itemService.update(itemUpdateRequest, itemResponse.getId(), userId).getAvailable(), false);
    }

    @Test
    public void getById() {
        assertEquals(itemService.getById(itemResponse.getId(), userId).getId(), itemResponse.getId());
    }

    @Test
    public void getByOwner() {
        assertTrue(itemService.getByOwner(userId, 0L, 10).size() > 0);
    }

    @Test
    public void getBySearch() {
        assertTrue(itemService.getBySearch("book", 0L, 10).size() > 0);
    }

    @Test
    public void createFailRequest() {
        itemCreateRequest.setRequestId(99L);
        assertThrows(NotFoundException.class, () -> itemService.create(itemCreateRequest, userId));
    }

    @Test
    public void createFailOwner() {
        assertThrows(NotFoundException.class, () -> itemService.create(itemCreateRequest, 99L));
    }

    @Test
    public void updateFailItem() {
        itemUpdateRequest = new ItemUpdateRequest("knife", null, null);
        assertThrows(NotFoundException.class, () -> itemService.update(itemUpdateRequest, 99L, userId));
    }

    @Test
    public void updateFailOwner() {
        itemUpdateRequest = new ItemUpdateRequest("knife", null, null);
        assertThrows(ForbiddenException.class, () -> itemService.update(itemUpdateRequest, itemResponse.getId(), 99L));
    }

    @Test
    public void getByOwnerFailOwner() {
        assertThrows(NotFoundException.class, () -> itemService.getByOwner(99L, 0L, 10));

    }

    @Test
    public void getByIdFailId() {
        assertThrows(NotFoundException.class, () -> itemService.getById(99L, userId));
    }

    @Test
    public void getBySearchEmpty() {
        assertEquals(0, itemService.getBySearch("", 0L, 10).size());
    }
}
