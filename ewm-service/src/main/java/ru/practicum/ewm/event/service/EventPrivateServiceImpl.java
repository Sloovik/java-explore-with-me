package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.enums.EventRequestStatusUpdate;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.enums.EventStateAction;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.service.LocationService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.stats.service.StatsClientService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserAdminRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserAdminRepository userAdminRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    private final LocationService locationService;
    private final EventUtilService eventUtilService;
    private final StatsClientService statsClientService;

    @Override
    public List<EventShortDto> getEvents(Long userId, Pageable pageable) {
        if (!userAdminRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %x not found", userId));
        }

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);

        if (events.isEmpty()) {
            return List.of();
        }

        return eventUtilService.getEventShortDtos(events);
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        log.info("EventPrivateServiceImpl addEvent. Params: userId: {}; newEventDto: {};", userId, newEventDto);

        User user = userAdminRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id %x not found", userId)));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new NotFoundException(String.format("Category with id %x not found", newEventDto.getCategory())));

        Location location = locationService.getWithSave(newEventDto.getLocation());

        log.info("EventPrivateServiceImpl addEvent. location: {};", location);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(0L, 0L,
                eventRepository.save(EventMapper.toEvent(user, category, location, newEventDto)));

        log.info("EventPrivateServiceImpl addEvent. eventFullDto: {};", eventFullDto);

        return eventFullDto;
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        log.info("EventPrivateServiceImpl addEvent. Params: userId: {}; eventId: {};", userId, eventId);

        if (!userAdminRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %x not found", userId));
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));
        Long view = statsClientService.getViewCountForEvent(event);
        Long requests = Long.valueOf(eventUtilService.getConfirmedRequestForEvent(event));

        log.info("EventPrivateServiceImpl addEvent. view: {}; requests: {};", view, requests);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(view, requests, event);

        log.info("EventPrivateServiceImpl addEvent. eventFullDto: {};", eventFullDto);

        return eventFullDto;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        log.info("EventPrivateServiceImpl updateEvent. Params: userId: {}; eventId: {}; updateEventDto: {};", userId, eventId, updateEventDto);

        if (!userAdminRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %x not found", userId));
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("It is not possible to edit events that have already been published");
        }

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }

        if (updateEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException(String.format("Category with id %x not found", updateEventDto.getCategory())));

            event.setCategory(category);
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getEventDate() != null) {
            if (updateEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Incorrect event date");
            }

            event.setEventDate(updateEventDto.getEventDate());
        }

        if (updateEventDto.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(updateEventDto.getLocation()));
        }

        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }

        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }

        if (updateEventDto.getStateAction() != null) {
            if (updateEventDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }

            if (updateEventDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }

        Long view = statsClientService.getViewCountForEvent(event);
        Long requests = Long.valueOf(eventUtilService.getConfirmedRequestForEvent(event));

        log.info("EventPrivateServiceImpl updateEvent. view: {}; requests: {};", view, requests);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(view, requests, event);

        log.info("EventPrivateServiceImpl updateEvent. eventFullDto: {};", eventFullDto);

        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("EventPrivateServiceImpl getEventRequests. Params: userId: {}; eventId: {};", userId, eventId);

        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new ConflictException("You are not the initiator of the event");
        }

        List<ParticipationRequest> requests = participationRequestRepository.findAllByEvent(eventId);

        log.info("EventPrivateServiceImpl getEventRequests. requests: {};", requests);

        List<ParticipationRequestDto> requestDtos = requests.stream()
                .map(ParticipationRequestMapper::toDto).collect(Collectors.toList());

        log.info("EventPrivateServiceImpl getEventRequests. requestDtos: {};", requestDtos);

        return requestDtos;
    }

    @Override
    public UpdateEventRequestStatusResultDto updateEventRequests(Long userId, Long eventId,
                                                                 UpdateEventRequestStatusDto updateEventRequestStatusDto) {
        log.info("EventPrivateServiceImpl updateEventRequests. Params: userId: {}; eventId: {}; updateEventRequestStatusDto: {};",
                userId, eventId, updateEventRequestStatusDto);

        if (!userAdminRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %x not found", userId));
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        log.info("EventPrivateServiceImpl updateEventRequests. event: {};", event);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("You are not the initiator of the event");
        }

        Long confirmedRequest = Long.valueOf(eventUtilService.getConfirmedRequestForEvent(event));

        log.info("EventPrivateServiceImpl updateEventRequests. confirmedRequest: {};", confirmedRequest);

        if (event.getParticipantLimit() != 0 && confirmedRequest >= event.getParticipantLimit()) {
            throw new ConflictException("Maximum participant level reached");
        }

        UpdateEventRequestStatusResultDto result = UpdateEventRequestStatusResultDto.builder().build();

        updateEventRequestStatusDto.getRequestIds().forEach((requestId) -> {
            ParticipationRequest request = participationRequestRepository.findPendingRequestsById(requestId).orElseThrow(() ->
                    new NotFoundException(String.format("Request with id %x not found", requestId)));

            log.info("EventPrivateServiceImpl updateEventRequests. request: {};", request);

            if (updateEventRequestStatusDto.getStatus().equals(EventRequestStatusUpdate.CONFIRMED)) {
                request.setStatus(RequestStatus.CONFIRMED);
                result.getConfirmedRequests().add(ParticipationRequestMapper.toDto(request));
            }

            if (updateEventRequestStatusDto.getStatus().equals(EventRequestStatusUpdate.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(ParticipationRequestMapper.toDto(request));
            }
        });

        if (!result.getConfirmedRequests().isEmpty()) {
            participationRequestRepository.saveAll(result.getConfirmedRequests().stream()
                    .map(ParticipationRequestMapper::toModel).collect(Collectors.toList()));
        }

        if (!result.getRejectedRequests().isEmpty()) {
            participationRequestRepository.saveAll(result.getRejectedRequests().stream()
                    .map(ParticipationRequestMapper::toModel).collect(Collectors.toList()));
        }

        log.info("EventPrivateServiceImpl updateEventRequests. result: {};", result);

        return result;
    }

}
