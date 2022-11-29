package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.responseDto.ItemRequestResponse;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestResponse fromRequestToResponse(ItemRequest itemRequest) {
        return ItemRequestResponse.builder()
                .created(itemRequest.getCreated())
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(ItemMapper.fromItemsToResponses(itemRequest.getItems()))
                .build();
    }

    public static List<ItemRequestResponse> fromRequestsToResponses(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::fromRequestToResponse)
                .collect(Collectors.toList());
    }
}
