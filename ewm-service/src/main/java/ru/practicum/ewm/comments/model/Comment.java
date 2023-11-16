package ru.practicum.ewm.comments.model;

import lombok.*;
import ru.practicum.ewm.comments.enums.CommentStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.MODERATING;

}
