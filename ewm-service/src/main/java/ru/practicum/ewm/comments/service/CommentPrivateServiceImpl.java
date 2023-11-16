package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.UpdateCommentDto;
import ru.practicum.ewm.comments.mapper.CommentMapper;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventUtilService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserAdminRepository;

@Service
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {

    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserAdminRepository userAdminRepository;

    private final EventUtilService eventUtilService;

    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userAdminRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id %x not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        Comment comment = CommentMapper.toComment(user, event, newCommentDto);

        return CommentMapper.toCommentDto(commentRepository.save(comment), eventUtilService.getEventShortDto(event));

    }

    public CommentDto updateComment(Long userId, Long eventId, UpdateCommentDto updateCommentDto) {
        User user = userAdminRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id %x not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        Comment comment = CommentMapper.toComment(user, event, updateCommentDto);

        return CommentMapper.toCommentDto(commentRepository.save(comment), eventUtilService.getEventShortDto(event));
    }

}
