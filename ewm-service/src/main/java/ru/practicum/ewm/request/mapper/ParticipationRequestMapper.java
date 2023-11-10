package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {

    public static ParticipationRequest toModel(Long userId, Long eventId, RequestStatus requestStatus) {
        return ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .status(requestStatus)
                .requester(userId)
                .event(eventId)
                .build();
    }

    public static ParticipationRequest toModel(ParticipationRequestDto dto) {
        return ParticipationRequest.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .requester(dto.getRequester())
                .status(dto.getStatus())
                .event(dto.getEvent())
                .build();
    }

    public static ParticipationRequestDto toDto(ParticipationRequest model) {
        return ParticipationRequestDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .requester(model.getRequester())
                .status(model.getStatus())
                .event(model.getEvent())
                .build();
    }

}
