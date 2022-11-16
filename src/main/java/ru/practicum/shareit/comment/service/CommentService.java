package ru.practicum.shareit.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingDbRepository;
import ru.practicum.shareit.comment.dto.requestDto.CommentCreateRequest;
import ru.practicum.shareit.comment.dto.responseDto.CommentResponse;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserDbRepository userRepository;
    private final ItemDbRepository itemRepository;

    private final BookingDbRepository bookingRepository;

    public CommentResponse create(CommentCreateRequest request, Long itemId, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + authorId + " is not found.");
        });
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException("Item with id: " + itemId + " is not found.");
        });
        if (bookingRepository.findByBookerAndItemAndStatus(author, item, BookingStatus.APPROVED).isEmpty()) {
            throw new ValidationException("User with id: " + authorId + " can't comment this item");
        }

        Comment comment = Comment.builder()
                .author(author)
                .item(item)
                .text(request.getText())
                .create(LocalDateTime.now())
                .build();

        return CommentMapper.fromCommentToResponse(commentRepository.save(comment));
    }

}
