package ru.practicum.ewm.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.comments.dto.CommentShortDto;
import ru.practicum.ewm.comments.service.CommentPublicService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentPublicController {

    private final CommentPublicService commentPublicService;

    @GetMapping("/events/{eventId}")
    public List<CommentShortDto> getCommentsForEvent(@PathVariable Long eventId) {
        return commentPublicService.getCommentsForEvent(eventId);
    }

}
