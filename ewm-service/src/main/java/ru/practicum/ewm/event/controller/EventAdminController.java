package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.SearchAdminEventsParamsDto;
import ru.practicum.ewm.event.dto.UpdateAdminEventDto;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.service.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class EventAdminController {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventAdminService eventAdminService;

    @GetMapping
    public List<EventFullDto> getAdminEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        SearchAdminEventsParamsDto searchAdminEventsParamsDto = SearchAdminEventsParamsDto.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .start(start)
                .end(end)
                .pageable(pageable)
                .build();

        log.info("GET getAdminEvents '/admin/events'. Params: searchAdminEventsParamsDto: {};", searchAdminEventsParamsDto);

        return eventAdminService.getEvents(searchAdminEventsParamsDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateAdminEventDto updateAdminEventDto) {
        log.info("PATCH updateEvent '/admin/events/{eventId}'. Params: eventId: {}; updateAdminEventDto: {};", eventId,
                updateAdminEventDto);

        return eventAdminService.updateEvent(eventId, updateAdminEventDto);
    }

}
