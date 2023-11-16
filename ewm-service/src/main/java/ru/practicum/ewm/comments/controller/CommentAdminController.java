package ru.practicum.ewm.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.ChangeCommentStatusDto;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.service.CommentAdminService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/comments")
public class CommentAdminController {

    private final CommentAdminService commentAdminService;

    @PatchMapping("/{commentId}")
    public CommentDto changeCommentStatus(@PathVariable Long commentId,
                                          @RequestBody ChangeCommentStatusDto changeCommentStatusDto) {
        return commentAdminService.changeCommentStatus(commentId, changeCommentStatusDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentAdminService.deleteComment(commentId);
    }

}
