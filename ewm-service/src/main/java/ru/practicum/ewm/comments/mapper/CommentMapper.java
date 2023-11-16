package ru.practicum.ewm.comments.mapper;

import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.CommentShortDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.UpdateCommentDto;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserAdminMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(User user, Event event, NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static Comment toComment(User user, Event event, UpdateCommentDto updateCommentDto) {
        return Comment.builder()
                .id(updateCommentDto.getId())
                .text(updateCommentDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment, EventShortDto eventShortDto) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(eventShortDto)
                .author(UserAdminMapper.toUserShortDto(comment.getAuthor()))
                .status(comment.getStatus())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .author(UserAdminMapper.toUserShortDto(comment.getAuthor()))
                .createdOn(comment.getCreatedOn())
                .build();
    }

}
