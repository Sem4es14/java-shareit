package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.responseDto.CommentResponse;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentResponse fromCommentToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .item(comment.getItem())
                .text(comment.getText())
                .created(comment.getCreate())
                .build();
    }

    public static List<CommentResponse> fromCommentsToResponses(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::fromCommentToResponse)
                .collect(Collectors.toList());
    }
}
