package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingDbRepository;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ResponseDto.ItemResponse;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemDbRepository itemRepository;
    private UserDbRepository userRepository;
    private BookingDbRepository bookingRepository;
    private CommentRepository commentRepository;

    @Override
    public ItemResponse create(ItemCreateRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + ownerId + " is not found.");
        });

        Item item = Item.builder()
                .name(request.getName())
                .available(request.getAvailable())
                .description(request.getDescription())
                .owner(owner)
                .request(request.getRequest())
                .build();

        return ItemMapper.fromItemToResponse(itemRepository.save(item));
    }

    @Override
    public ItemResponse update(ItemUpdateRequest request, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException("Item with id: " + itemId + " is not found.");
        });
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("Access to item with id: " + itemId + " is forbidden");
        }
        item.setName(request.getName() == null ? item.getName() : request.getName());
        item.setDescription(request.getDescription() == null ? item.getDescription() : request.getDescription());
        item.setAvailable(request.getAvailable() == null ? item.getAvailable() : request.getAvailable());

        return ItemMapper.fromItemToResponse(itemRepository.save(item));
    }

    @Override
    public List<ItemResponse> getByOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + ownerId + " is not found.");
        });
        List<Item> items = itemRepository.findByOwner(owner);
        List<ItemResponse> responses = new ArrayList<>();
        for (Item item : items) {
            ItemResponse itemResponse = ItemMapper.fromItemToResponse(item);
            itemResponse.setLastBooking(BookingMapper.fromBookingToShortResponse(
                    bookingRepository.findByItemAndEndBefore(item, LocalDateTime.now()).orElse(null)
            ));
            itemResponse.setNextBooking(BookingMapper.fromBookingToShortResponse(
                    bookingRepository.findByItemAndStartAfter(item, LocalDateTime.now()).orElse(null)
            ));
            itemResponse.setComments(CommentMapper.fromCommentsToResponses(commentRepository.findByItem(item)));
            responses.add(itemResponse);
        }

        return responses;
    }

    @Override
    public ItemResponse getById(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Item with id: " + id + " is not found.");
        });
        ItemResponse itemResponse = ItemMapper.fromItemToResponse(item);
        if (item.getOwner().getId().equals(userId)) {
            itemResponse.setLastBooking(BookingMapper.fromBookingToShortResponse(
                    bookingRepository.findByItemAndEndBefore(item, LocalDateTime.now()).orElse(null)
            ));
            itemResponse.setNextBooking(BookingMapper.fromBookingToShortResponse(
                    bookingRepository.findByItemAndStartAfter(item, LocalDateTime.now()).orElse(null)
            ));
        }
        itemResponse.setComments(CommentMapper.fromCommentsToResponses(commentRepository.findByItem(item)));
;
        return itemResponse;
    }

    @Override
    public List<ItemResponse> getBySearch(String search) {
        if (search.isEmpty()) {
            return Collections.emptyList();
        }

        return ItemMapper.fromItemsToResponses(itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(search, search));
    }
}
