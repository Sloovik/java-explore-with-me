package ru.practicum.ewm.comments.service;

import ru.practicum.ewm.comments.dto.CommentShortDto;

import java.util.List;

public interface CommentPublicService {

    List<CommentShortDto> getCommentsForEvent(Long eventId);

}
