package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.responseDto.BookingResponse;
import ru.practicum.shareit.booking.dto.responseDto.BookingShortResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingResponse fromBookingToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingResponse> fromBookingsToResponses(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::fromBookingToResponse)
                .collect(Collectors.toList());
    }

    public static BookingShortResponse fromBookingToShortResponse(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingShortResponse.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
