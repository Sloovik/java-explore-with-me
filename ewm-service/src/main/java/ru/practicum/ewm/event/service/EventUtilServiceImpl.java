package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.stats.service.StatsClientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventUtilServiceImpl implements EventUtilService {

    private final ParticipationRequestRepository participationRequestRepository;

    private final StatsClientService statsClientService;

    @Override
    public Integer getConfirmedRequestForEvent(Event event) {
        return participationRequestRepository.findConfirmedRequestsForEvent(event.getId()).size();
    }

    @Override
    public Map<Long, Long> getConfirmedRequestForEventList(List<Event> events) {
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        List<ParticipationRequest> requests = participationRequestRepository.findConfirmedRequestsForEventList(eventIds);

        Map<Long, Long> result = new HashMap<>();

        for (ParticipationRequest request : requests) {
            if (result.containsKey(request.getEvent())) {
                result.put(request.getEvent(), result.get(request.getEvent()) + 1);
            } else {
                result.put(request.getEvent(), 1L);
            }
        }

        return result;
    }

    @Override
    public EventShortDto getEventShortDto(Event event) {
        Long views = statsClientService.getViewCountForEvent(event);
        Long requests = Long.valueOf(getConfirmedRequestForEvent(event));

        return EventMapper.toEventShortDto(views, requests, event);
    }

    @Override
    public List<EventShortDto> getEventShortDtos(List<Event> events) {
        Map<Long, Long> views = statsClientService.getViewCountForEvents(events);
        Map<Long, Long> requests = getConfirmedRequestForEventList(events);
        List<EventShortDto> eventShortDtos = new ArrayList<>();

        for (Event event : events) {
            eventShortDtos.add(EventMapper.toEventShortDto(views.get(event.getId()), requests.get(event.getId()), event));
        }

        return eventShortDtos;
    }

    @Override
    public List<EventFullDto> getEventFullDtos(List<Event> events) {
        Map<Long, Long> views = statsClientService.getViewCountForEvents(events);
        Map<Long, Long> requests = getConfirmedRequestForEventList(events);
        List<EventFullDto> eventFullDtos = new ArrayList<>();

        for (Event event : events) {
            eventFullDtos.add(EventMapper.toEventFullDto(views.get(event.getId()), requests.get(event.getId()), event));
        }

        return eventFullDtos;
    }

}
