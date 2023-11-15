package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface StatsClientService {

    Long getViewCountForEvent(Event event);

    Map<Long, Long> getViewCountForEvents(List<Event> events);

    void addHits(HttpServletRequest request);

}
