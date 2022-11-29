package ru.practicum.shareit.request.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.responseDto.ItemResponse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestResponse {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponse> items;
}
