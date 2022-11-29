package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemResponse fromItemToResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .request(item.getRequest())
                .available(item.getAvailable())
                .description(item.getDescription())
                .owner(item.getOwner())
                .build();
    }

    public static List<ItemResponse> fromItemsToResponses(List<Item> items) {
        return items.stream()
                .map(ItemMapper::fromItemToResponse)
                .collect(Collectors.toList());
    }
}
