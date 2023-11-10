package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.SearchAdminEventsParamsDto;
import ru.practicum.ewm.event.dto.UpdateAdminEventDto;

import java.util.List;

public interface EventAdminService {

    List<EventFullDto> getEvents(SearchAdminEventsParamsDto searchAdminEventsParamsDto);


    EventFullDto updateEvent(Long eventId, UpdateAdminEventDto updateAdminEventDto);

}
