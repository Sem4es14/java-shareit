package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ResponseDto.ItemResponse;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public ItemResponse create(ItemCreateRequest request, Long ownerId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("User with id: " + ownerId + " is not found.");
        }
        Item item = Item.builder()
                .name(request.getName())
                .available(request.getAvailable())
                .description(request.getDescription())
                .owner(ownerId)
                .request(request.getRequest())
                .build();

        return ItemMapper.fromItemToResponse(itemRepository.save(item));
    }

    @Override
    public ItemResponse update(ItemUpdateRequest request, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException("Item with id: " + itemId + " is not found.");
        });
        if (!item.getOwner().equals(ownerId)) {
            throw new ForbiddenException("Access to item with id: " + itemId + " is forbidden");
        }
        item.setName(request.getName() == null ? item.getName() : request.getName());
        item.setDescription(request.getDescription() == null ? item.getDescription() : request.getDescription());
        item.setAvailable(request.getAvailable() == null ? item.getAvailable() : request.getAvailable());

        return ItemMapper.fromItemToResponse(itemRepository.save(item));
    }

    @Override
    public List<ItemResponse> getByOwner(Long ownerId) {
        return ItemMapper.fromItemsToResponses(itemRepository.findByOwner(ownerId));
    }

    @Override
    public ItemResponse getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Item with id: " + id + " is not found.");
        });

        return ItemMapper.fromItemToResponse(item);
    }

    @Override
    public List<ItemResponse> getBySearch(String search) {
        if (search.isEmpty()) {
            return Collections.emptyList();
        }

        return ItemMapper.fromItemsToResponses(itemRepository.findBySearch(search));
    }
}
