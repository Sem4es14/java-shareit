package ru.practicum.shareit.booking.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Long bookerId;
    private Long itemId;
}
