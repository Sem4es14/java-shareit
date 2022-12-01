package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.requestDto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.requestDto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.responseDto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;
import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.requestDto.UserCreateRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ItemService itemService;
    private BookingCreateRequest createRequest;
    private BookingUpdateRequest updateRequest;
    private UserCreateRequest userCreateRequest;
    private ItemResponse itemResponse;
    private BookingResponse response;
    private Long bookerId;
    private Long ownerId;

    @BeforeEach
    public void beforeEach() {
        userCreateRequest = new UserCreateRequest("ivan", "ivanov@gmail.com");
        ownerId = userService.create(userCreateRequest).getId();
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("book", "beautiful book", true, null);
        userCreateRequest.setEmail("ivanov2@gmail.com");
        bookerId = userService.create(userCreateRequest).getId();
        itemResponse = itemService.create(itemCreateRequest, ownerId);
        createRequest = new BookingCreateRequest(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                bookerId,
                itemResponse.getId()
        );
    }

    @Test
    public void create() {
        response = bookingService.create(createRequest, bookerId);
        assertEquals(response.getStart(), createRequest.getStart());
    }

    @Test
    public void updateStatus() {
        response = bookingService.create(createRequest, bookerId);
        assertEquals(response.getStatus(), BookingStatus.WAITING);
        response = bookingService.updateStatus(response.getId(), ownerId, true);
        assertEquals(response.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    public void getById() {
        response = bookingService.create(createRequest, bookerId);
        assertEquals(response.getId(), bookingService.getById(response.getId(), ownerId).getId());
    }

    @Test
    public void getByBookerAll() {
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingsByBooker(bookerId, BookingState.ALL, 0L, 10).size() > 0);
    }

    @Test
    public void getByBookerCurrent() {
        createRequest.setStart(LocalDateTime.now().minusDays(1));
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingsByBooker(bookerId, BookingState.CURRENT, 0L, 10).size() > 0);
    }

    @Test
    public void getByBookerPast() {
        createRequest.setEnd(LocalDateTime.now().minusDays(1));
        createRequest.setStart(LocalDateTime.now().minusDays(2));
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingsByBooker(bookerId, BookingState.PAST, 0L, 10).size() > 0);
    }

    @Test
    public void getByBookerFuture() {
        createRequest.setStart(LocalDateTime.now().plusHours(1));
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingsByBooker(bookerId, BookingState.FUTURE, 0L, 10).size() > 0);
    }

    @Test
    public void getByBookerWaiting() {
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingsByBooker(bookerId, BookingState.WAITING, 0L, 10).size() > 0);
    }

    @Test
    public void getByBookerRejected() {
        response = bookingService.create(createRequest, bookerId);
        bookingService.updateStatus(response.getId(), ownerId, false);
        assertTrue(bookingService.getBookingsByBooker(bookerId, BookingState.REJECTED, 0L, 10).size() > 0);
    }

    @Test
    public void getByOwnerAll() {
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingByOwner(ownerId, BookingState.ALL, 0L, 10).size() > 0);
    }

    @Test
    public void getByOwnerCurrent() {
        createRequest.setStart(LocalDateTime.now().minusDays(1));
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingByOwner(ownerId, BookingState.CURRENT, 0L, 10).size() > 0);
    }

    @Test
    public void getByOwnerPast() {
        createRequest.setEnd(LocalDateTime.now().minusDays(1));
        createRequest.setStart(LocalDateTime.now().minusDays(2));
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingByOwner(ownerId, BookingState.PAST, 0L, 10).size() > 0);
    }

    @Test
    public void getByOwnerFuture() {
        createRequest.setStart(LocalDateTime.now().plusHours(1));
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingByOwner(ownerId, BookingState.FUTURE, 0L, 10).size() > 0);
    }

    @Test
    public void getByOwnerWaiting() {
        response = bookingService.create(createRequest, bookerId);
        assertTrue(bookingService.getBookingByOwner(ownerId, BookingState.WAITING, 0L, 10).size() > 0);
    }

    @Test
    public void getByOwnerRejected() {
        response = bookingService.create(createRequest, bookerId);
        bookingService.updateStatus(response.getId(), ownerId, false);
        assertTrue(bookingService.getBookingByOwner(ownerId, BookingState.REJECTED, 0L, 10).size() > 0);
    }

    @Test
    public void createWithFailUser() {
        assertThrows(NotFoundException.class, () -> bookingService.create(createRequest, 99L));
    }

    @Test
    public void createWithFailItem() {
        createRequest.setItemId(99L);
        assertThrows(NotFoundException.class, () -> bookingService.create(createRequest, bookerId));
    }

    @Test
    public void updateStatusFailBooking() {
        assertThrows(NotFoundException.class, () -> bookingService.updateStatus(99L, ownerId, true));
    }

    @Test
    public void updateStatusFailOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.updateStatus(1L, 99L, true));
    }

    @Test
    public void createFailEndDate() {
        createRequest.setEnd(LocalDateTime.now().minusDays(10));
        assertThrows(ValidationException.class, () -> bookingService.create(createRequest, bookerId));
    }

    @Test
    public void createFailAvailable() {
        ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest("name", "desc", false);
        itemService.update(itemUpdateRequest, itemResponse.getId(), ownerId);
        assertThrows(ValidationException.class, () -> bookingService.create(createRequest, bookerId));
        itemUpdateRequest.setAvailable(true);
        itemService.update(itemUpdateRequest, itemResponse.getId(), ownerId);
    }

    @Test
    public void createFailBooker() {
        assertThrows(NotFoundException.class, () -> bookingService.create(createRequest, ownerId));
    }

    @Test
    public void updateStatusNotOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.updateStatus(1L, bookerId, true));
    }

    @Test
    public void getByIdFail() {
        assertThrows(NotFoundException.class, () -> bookingService.getById(99L, ownerId));
    }

    @Test
    public void getByIdFailUser() {
        userCreateRequest.setEmail("ivanov3@gmail.com");
        Long userId = userService.create(userCreateRequest).getId();
        response = bookingService.create(createRequest, bookerId);
        assertThrows(NotFoundException.class, () -> bookingService.getById(response.getId(), userId));
    }

    @Test
    public void getByBookerAllFailBooker() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsByBooker(99L, BookingState.ALL, 0L, 10));
    }

    @Test
    public void getByOwnerAllFailOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingByOwner(99L, BookingState.ALL, 0L, 10));
    }
}
