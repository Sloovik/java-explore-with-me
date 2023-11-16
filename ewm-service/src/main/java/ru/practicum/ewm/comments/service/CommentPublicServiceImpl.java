package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comments.dto.CommentShortDto;
import ru.practicum.ewm.comments.mapper.CommentMapper;
import ru.practicum.ewm.comments.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentPublicServiceImpl implements CommentPublicService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentShortDto> getCommentsForEvent(Long eventId) {
        return commentRepository.findAllByEventId(eventId).stream().map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }

}
