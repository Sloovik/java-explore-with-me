package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.SearchAdminEventsParamsDto;
import ru.practicum.ewm.event.dto.UpdateAdminEventDto;
import ru.practicum.ewm.event.enums.EventAdminStateAction;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.location.service.LocationService;
import ru.practicum.ewm.stats.service.StatsClientService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private final LocationService locationService;
    private final EventUtilService eventUtilService;
    private final StatsClientService statsClientService;

    @Override
    public List<EventFullDto> getEvents(SearchAdminEventsParamsDto searchAdminEventsParamsDto) {
        log.info("EventAdminServiceImpl getEvents. Params: searchAdminEventsParamsDto: {};", searchAdminEventsParamsDto);

        List<Long> users = searchAdminEventsParamsDto.getUsers();
        List<EventState> states = searchAdminEventsParamsDto.getStates();
        List<Long> categories = searchAdminEventsParamsDto.getCategories();
        LocalDateTime start = searchAdminEventsParamsDto.getStart();
        LocalDateTime end = searchAdminEventsParamsDto.getEnd();
        Pageable pageable = searchAdminEventsParamsDto.getPageable();

        Specification<Event> specification = Specification.where(null);

        if (users != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }

        if (states != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("state").in(states));
        }

        if (categories != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }

        if (start != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start));
        }

        if (end != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
        }

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();

        log.info("EventAdminServiceImpl getEvents. events: {};", events);

        if (events.isEmpty()) {
            return List.of();
        }

        return eventUtilService.getEventFullDtos(events);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateAdminEventDto updateAdminEventDto) {
        log.info("EventAdminServiceImpl updateEvent. Params: eventId: {}; updateAdminEventDto: {};", eventId, updateAdminEventDto);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        log.info("EventAdminServiceImpl updateEvent. event: {};", event);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("This event is already in progress or has passed, it cannot be changed");
        }

        if (updateAdminEventDto.getEventDate() != null) {
            if (updateAdminEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("The data for updating the event date is incorrect.");
            } else {
                event.setEventDate(updateAdminEventDto.getEventDate());
            }
        }

        if (updateAdminEventDto.getStateAction() != null) {
            if (!event.getState().equals(EventState.PENDING)) {
                throw new ConflictException("Сan not modify an event that is not in the status of pending, published, or canceled");
            }

            if (updateAdminEventDto.getStateAction().equals(EventAdminStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now().withNano(0));
            }

            if (updateAdminEventDto.getStateAction().equals(EventAdminStateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateAdminEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateAdminEventDto.getRequestModeration());
        }

        if (updateAdminEventDto.getPaid() != null) {
            event.setPaid(updateAdminEventDto.getPaid());
        }

        if (updateAdminEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateAdminEventDto.getParticipantLimit());
        }

        if (updateAdminEventDto.getLocation() != null) {
            event.setLocation(locationService.getWithSave(updateAdminEventDto.getLocation()));
        }

        if (updateAdminEventDto.getAnnotation() != null && !updateAdminEventDto.getTitle().isBlank()) {
            event.setAnnotation(updateAdminEventDto.getAnnotation());
        }

        if (updateAdminEventDto.getDescription() != null && !updateAdminEventDto.getDescription().isBlank()) {
            event.setDescription(updateAdminEventDto.getDescription());
        }

        if (updateAdminEventDto.getTitle() != null && !updateAdminEventDto.getTitle().isBlank()) {
            event.setTitle(updateAdminEventDto.getTitle());
        }

        if (updateAdminEventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateAdminEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Category with id %x not found", updateAdminEventDto.getCategory()))));
        }

        Long view = statsClientService.getViewCountForEvent(event);
        Long requests = Long.valueOf(eventUtilService.getConfirmedRequestForEvent(event));

        log.info("EventAdminServiceImpl updateEvent. view: {}; requests: {};", view, requests);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(view, requests, eventRepository.save(event));

        log.info("EventAdminServiceImpl updateEvent. eventFullDto: {};", eventFullDto);

        return eventFullDto;
    }

}
