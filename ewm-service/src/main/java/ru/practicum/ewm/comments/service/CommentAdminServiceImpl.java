package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comments.dto.ChangeCommentStatusDto;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.mapper.CommentMapper;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.repository.CommentRepository;
import ru.practicum.ewm.event.service.EventUtilService;
import ru.practicum.ewm.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService {

    private final CommentRepository commentRepository;

    private final EventUtilService eventUtilService;

    @Override
    public CommentDto changeCommentStatus(Long commentId, ChangeCommentStatusDto changeCommentStatusDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id %x not found", commentId)));

        comment.setStatus(changeCommentStatusDto.getStatus());

        return CommentMapper.toCommentDto(commentRepository.save(comment),
                eventUtilService.getEventShortDto(comment.getEvent()));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
