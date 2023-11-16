package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Map;

public interface EventUtilService {

    Integer getConfirmedRequestForEvent(Event event);

    Map<Long, Long> getConfirmedRequestForEventList(List<Event> events);

    EventShortDto getEventShortDto(Event event);

    List<EventShortDto> getEventShortDtos(List<Event> events);

    List<EventFullDto> getEventFullDtos(List<Event> events);

}
