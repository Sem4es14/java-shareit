package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;

import java.util.List;

public interface ItemService {
    ItemResponse create(ItemCreateRequest request, Long ownerId);

    ItemResponse update(ItemUpdateRequest request, Long itemId, Long ownerId);

    List<ItemResponse> getByOwner(Long ownerId, Long from, Integer size);

    ItemResponse getById(Long id, Long userId);

    List<ItemResponse> getBySearch(String search, Long from, Integer size);
}
