package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getPrivateEvents(@PathVariable Long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET getPrivateEvents '/users/{userId}/events'. Params: userId: {}; from: {}; size: {};", userId, from, size);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        return eventPrivateService.getEvents(userId, pageable);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST addEvent '/users/{userId}/events'. Params: userId: {}; newEventDto: {};", userId, newEventDto);

        return eventPrivateService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET getEvent '/users/{userId}/events/{eventId}'. Params: userId: {}; eventId: {};", userId, eventId);

        return eventPrivateService.getEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("PATCH updateEvent '/users/{userId}/events/{eventId}'. Params: userId: {}; eventId: {}; updateEventDto: {};",
                userId, eventId, updateEventDto);

        return eventPrivateService.updateEvent(userId, eventId, updateEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET getEventRequests '/users/{userId}/events/{eventId}/requests'. Params: userId: {}; eventId: {};",
                userId, eventId);

        return eventPrivateService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public UpdateEventRequestStatusResultDto updateEventRequests(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @Valid @RequestBody UpdateEventRequestStatusDto updateEventRequestStatusDto) {
        log.info("PATCH updateEventRequests '/users/{userId}/events/{eventId}/requests'. Params: userId: {}; " +
                "eventId: {}; updateEventRequestStatusDto: {};", userId, eventId, updateEventRequestStatusDto);

        return eventPrivateService.updateEventRequests(userId, eventId, updateEventRequestStatusDto);
    }

}
