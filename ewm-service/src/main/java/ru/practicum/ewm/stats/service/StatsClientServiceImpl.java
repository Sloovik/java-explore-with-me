package ru.practicum.ewm.stats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.StatsParseException;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.StatRequest;
import ru.practicum.ewm.StatResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsClientServiceImpl implements StatsClientService {

    private final StatsClient statsClient;

    private final ObjectMapper objectMapper;

    @Value("${app_name}")
    private String app;

    public Long getViewCountForEvent(Event event) {
        log.info("StatsClientServiceImpl. getViewCountForEvent. Params: event: {};", event);

        ResponseEntity<Object> response = statsClient.getStats(event.getCreatedOn(), LocalDateTime.now(),
                List.of("/events/" + event.getId()), true);

        log.info("StatsClientServiceImpl. getViewCountForEvent. response: {};", response);

        try {
            List<StatResponse> statResponse = objectMapper
                    .readValue(objectMapper.writeValueAsString(response.getBody()), new TypeReference<>() {
                    });

            log.info("StatsClientServiceImpl. getViewCountForEvent. statResponse: {};", statResponse);

            if (statResponse.isEmpty()) {
                return 0L;
            }

            return statResponse.get(0).getHits();
        } catch (JsonProcessingException e) {
            throw new StatsParseException("Error parse statistic");
        }
    }

    public Map<Long, Long> getViewCountForEvents(List<Event> events) {
        log.info("StatsClientServiceImpl. getViewCountForEventList. Params: events: {};", events);

        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(LocalDateTime::compareTo).orElse(null);

        log.info("StatsClientServiceImpl. getViewCountForEventList. start: {};", start);

        if (start == null) {
            return Map.of();
        }

        Map<Long, Long> view = new HashMap<>();
        List<String> uris = events.stream().map((e) -> "/events/" + e.getId()).collect(Collectors.toList());

        log.info("StatsClientServiceImpl. getViewCountForEventList. uris: {};", uris);

        ResponseEntity<Object> response = statsClient.getStats(start, LocalDateTime.now(), uris, true);

        log.info("StatsClientServiceImpl. getViewCountForEventList. response: {};", response);

        try {
            List<StatResponse> statResponse = objectMapper
                    .readValue(objectMapper.writeValueAsString(response.getBody()), new TypeReference<>() {
                    });

            log.info("StatsClientServiceImpl. getViewCountForEventList. statResponse: {};", statResponse);

            for (StatResponse stat : statResponse) {
                view.put(Long.parseLong(stat.getUri().replaceAll("\\D+", "")), stat.getHits());
            }

            log.info("StatsClientServiceImpl. getViewCountForEventList. view: {};", view);

            return view;
        } catch (JsonProcessingException e) {
            log.info("StatsClientServiceImpl. getViewCountForEventList. ERROR: {};", e.getMessage());

            throw new StatsParseException("Error parse statistic." + e.getMessage());
        }
    }

    public void addHits(HttpServletRequest request) {
        log.info("StatsClientServiceImpl. getViewCountForEventList. request: {}; getRequestURI: {};", request, request.getRequestURI());

        statsClient.postStats(StatRequest.builder()
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .app(app)
                .build());
    }

}
