package ru.practicum.shareit.coments;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.requestDto.BookingCreateRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.requestDto.CommentCreateRequest;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceTest {
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final UserService userService;
    private ItemResponse itemResponse;
    private CommentCreateRequest commentCreateRequest;
    private BookingCreateRequest bookingCreateRequest;
    private Long userId1;
    private Long bookerId;

    @BeforeEach
    public void beforeEach() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("ivan", "vanishvanish@mail.com");
        userId1 = userService.create(userCreateRequest).getId();
        userCreateRequest.setEmail("vavanish@mail.com");
        bookerId = userService.create(userCreateRequest).getId();
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("book", "beautiful book", true, null);
        itemResponse = itemService.create(itemCreateRequest, userId1);
        commentCreateRequest = new CommentCreateRequest("wow");
        bookingCreateRequest = new BookingCreateRequest(LocalDateTime.now(), LocalDateTime.now().plusNanos(1L), bookerId, itemResponse.getId());
    }

    @Test
    public void create() {
        Long bookingId = bookingService.create(bookingCreateRequest, bookerId).getId();
        bookingService.updateStatus(bookingId, itemResponse.getOwner().getId(), true);
        assertEquals(commentService.create(commentCreateRequest, itemResponse.getId(), bookerId).getId(), 1L);
    }

    @Test
    public void createUserCantComment() {
        assertThrows(ValidationException.class, () -> commentService.create(commentCreateRequest, itemResponse.getId(), userId1));
    }

    @Test
    public void createFailUser() {
        assertThrows(NotFoundException.class, () -> commentService.create(commentCreateRequest, itemResponse.getId(), 99L));
    }

    @Test
    public void createFailItem() {
        assertThrows(NotFoundException.class, () -> commentService.create(commentCreateRequest, 99L, bookerId));
    }
}
