package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.StatRequest;
import ru.practicum.stats.dto.StatResponse;
import ru.practicum.stats.server.service.StatsService;

import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/")
public class StatsController {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    @GetMapping("stats")
    public List<StatResponse> getStats(@RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                       @RequestParam(defaultValue = "") List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        if (end.isBefore(start)) {
            throw new InvalidParameterException("End date is before start date");
        }

        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody StatRequest statRequest) {
        statsService.save(statRequest);
    }

}
