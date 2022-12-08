package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.requestdto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.responsedto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingCreateRequest request,
                                                         @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return ResponseEntity.of(Optional.of(bookingService.create(request, ownerId)));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> updateStatus(@PathVariable Long bookingId,
                                                        @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(value = "approved") Boolean approved) {
        return ResponseEntity.of(Optional.of(bookingService.updateStatus(bookingId, ownerId, approved)));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long bookingId,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(bookingService.getById(bookingId, userId)));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getByBooker(@RequestParam(defaultValue = "0") Long from,
                                                             @RequestParam(defaultValue = "10") Integer size,
                                                             @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(bookingService.getBookingsByBooker(userId, state, from, size)));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponse>> getByOwner(@RequestParam(defaultValue = "0") Long from,
                                                            @RequestParam(defaultValue = "10") Integer size,
                                                            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(bookingService.getBookingByOwner(userId, state, from, size)));
    }
}
