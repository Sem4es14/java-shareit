package ru.practicum.shareit.item.dto.requestdto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
