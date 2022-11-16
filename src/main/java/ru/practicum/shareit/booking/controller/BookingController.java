package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.requestDto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.requestDto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.responseDto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingCreateRequest request,
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
    public ResponseEntity<BookingResponse> getByid(@PathVariable Long bookingId,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(bookingService.getById(bookingId, userId)));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getByBooker(@RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(bookingService.getBookingsByBooker(userId, state)));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponse>> getByOwner(@RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.of(Optional.of(bookingService.getBookingByOwner(userId, state)));
    }
}
