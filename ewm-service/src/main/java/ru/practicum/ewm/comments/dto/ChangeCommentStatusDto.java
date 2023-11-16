package ru.practicum.ewm.comments.dto;

import lombok.*;
import ru.practicum.ewm.comments.enums.CommentStatus;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCommentStatusDto {

    private CommentStatus status;

}
