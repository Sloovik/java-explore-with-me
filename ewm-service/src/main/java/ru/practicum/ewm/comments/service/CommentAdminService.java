package ru.practicum.ewm.comments.service;

import ru.practicum.ewm.comments.dto.ChangeCommentStatusDto;
import ru.practicum.ewm.comments.dto.CommentDto;

public interface CommentAdminService {

    CommentDto changeCommentStatus(Long commentId, ChangeCommentStatusDto changeCommentStatusDto);

    void deleteComment(Long commentId);

}
