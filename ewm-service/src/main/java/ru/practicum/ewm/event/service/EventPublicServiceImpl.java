package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.SearchPublicEventsParamsDto;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.stats.service.StatsClientService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {

    private final EventRepository eventRepository;

    private final EventUtilService eventUtilService;
    private final StatsClientService statsClientService;

    @Override
    public List<EventShortDto> getEvents(SearchPublicEventsParamsDto searchPublicEventsParamsDto) {
        log.info("EventPublicServiceImpl getEvents. Params: searchPublicEventsParamsDto: {};", searchPublicEventsParamsDto);

        String text = searchPublicEventsParamsDto.getText();
        List<Long> categories = searchPublicEventsParamsDto.getCategories();
        Boolean paid = searchPublicEventsParamsDto.getPaid();
        LocalDateTime rangeStart = searchPublicEventsParamsDto.getRangeStart();
        LocalDateTime rangeEnd = searchPublicEventsParamsDto.getRangeEnd();
        Boolean onlyAvailable = searchPublicEventsParamsDto.getOnlyAvailable();
        Pageable pageable = searchPublicEventsParamsDto.getPageable();
        HttpServletRequest request = searchPublicEventsParamsDto.getRequest();

        Specification<Event> specification = Specification.where(null);

        if (text != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                    ));
        }

        if (categories != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }

        if (paid != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paid"), paid));
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rangeStartDateTime = Objects.requireNonNullElse(rangeStart, now);

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStartDateTime));

        if (rangeEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        if (onlyAvailable != null && onlyAvailable) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        specification = specification.and((root, query, criteriaBuilder) ->
                root.get("state").in(EventState.PUBLISHED));

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();

        log.info("EventPublicServiceImpl getEvents. events: {};", events);

        if (events.isEmpty()) {
            return List.of();
        }

        List<EventShortDto> eventShortDtos = eventUtilService.getEventShortDtos(events);

        statsClientService.addHits(request);

        log.info("EventPublicServiceImpl getEvents. eventShortDtos: {};", eventShortDtos);

        return eventShortDtos;
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        log.info("EventPublicServiceImpl getEvent. Params: eventId: {}; request: {};", eventId, request);

        Event event = eventRepository.findPublishedById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        Long view = statsClientService.getViewCountForEvent(event);
        Long requests = Long.valueOf(eventUtilService.getConfirmedRequestForEvent(event));

        log.info("EventPublicServiceImpl getEvent. view: {}; request: {};", view, requests);

        statsClientService.addHits(request);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(view, requests, event);

        log.info("EventPublicServiceImpl getEvent. eventFullDto: {};", eventFullDto);

        return eventFullDto;
    }

}
