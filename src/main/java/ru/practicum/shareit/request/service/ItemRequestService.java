package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.request.dto.requestDto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.responseDto.ItemRequestResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserDbRepository userRepository;
    private final ItemDbRepository itemRepository;

    public ItemRequestResponse createRequest(ItemRequestCreate request, Long requesterId) {
        User requester = userRepository.findById(requesterId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + requesterId + " is not found.");
        });
        ItemRequest itemRequest = ItemRequest.builder()
                .description(request.getDescription())
                .requester(requester)
                .items(itemRepository.findByRequest(requesterId))
                .created(LocalDateTime.now())
                .build();

        return ItemRequestMapper.fromRequestToResponse(requestRepository.save(itemRequest));
    }

    public List<ItemRequestResponse> getByRequester(Long requesterId) {
        User requester = userRepository.findById(requesterId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + requesterId + " is not found.");
        });

        return ItemRequestMapper.fromRequestsToResponses(requestRepository.findByRequester(requester));
    }

    public List<ItemRequestResponse> getAll(Long requesterId, Long from, Integer size) {
        User requester = userRepository.findById(requesterId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + requesterId + " is not found.");
        });
        Pageable pageable = PageRequest.of((int) (from/size), size, Sort.by("created").descending());

        return ItemRequestMapper.fromRequestsToResponses(requestRepository.findAllByRequesterNot(pageable, requester).getContent());
    }

    public ItemRequestResponse getById(Long id, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found.");
        });
        ItemRequest itemRequest = requestRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("ItemRequest with id: " + id + " is not found.");
        });

        return ItemRequestMapper.fromRequestToResponse(itemRequest);
    }
}
