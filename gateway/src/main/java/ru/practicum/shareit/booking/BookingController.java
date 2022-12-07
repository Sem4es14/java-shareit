package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                              @RequestParam(name = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {

        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getByBooker(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookingCreateRequest requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwner(@RequestParam(defaultValue = "0")@Min(value = 0) Integer from,
                                             @RequestParam(defaultValue = "10")@Positive Integer size,
                                             @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.getByOwner(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@PathVariable Long bookingId,
                                               @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(value = "approved") Boolean approved) {
        return bookingClient.updateStatus(bookingId, ownerId, approved);
    }
}
