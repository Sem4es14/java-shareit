package ru.practicum.shareit.item.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.responseDto.BookingShortResponse;
import ru.practicum.shareit.comment.dto.responseDto.CommentResponse;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long request;
    private BookingShortResponse lastBooking;
    private BookingShortResponse nextBooking;
    private List<CommentResponse> comments;
}
