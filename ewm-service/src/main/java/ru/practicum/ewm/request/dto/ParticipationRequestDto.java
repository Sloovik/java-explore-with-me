package ru.practicum.ewm.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.request.enums.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class ParticipationRequestDto {

    private Long id;

    private LocalDateTime created;

    private Long event;

    private Long requester;

    private RequestStatus status;

}
