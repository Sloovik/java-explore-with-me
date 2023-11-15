package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.SearchPublicEventsParamsDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventPublicService {

    List<EventShortDto> getEvents(SearchPublicEventsParamsDto searchPublicEventsParamsDto);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

}
