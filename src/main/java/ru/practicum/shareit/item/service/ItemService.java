package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;

import java.util.List;

public interface ItemService {
    ItemResponse create(ItemCreateRequest request, Long ownerId);

    ItemResponse update(ItemUpdateRequest request, Long itemId, Long ownerId);

    ItemResponse getById(Long id, Long userId);

    List<ItemResponse> getByOwner(Long ownerId);

    List<ItemResponse> getBySearch(String search);
}
