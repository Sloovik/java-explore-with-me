package ru.practicum.ewm.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {

    List<EventShortDto> getEvents(Long userId, Pageable pageable);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    UpdateEventRequestStatusResultDto updateEventRequests(Long userId, Long eventId,
                                                          UpdateEventRequestStatusDto updateEventRequestStatusDto);

}
