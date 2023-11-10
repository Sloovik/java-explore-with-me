package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.StatRequest;
import ru.practicum.ewm.StatResponse;
import ru.practicum.ewm.service.StatsService;

import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        log.info("Stats Server. getStats. Params: start: {}; end: {}; uris: {}; unique: {};", start, end, uris, unique);

        if (end.isBefore(start)) {
            throw new InvalidParameterException("End date is before start date");
        }

        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody StatRequest statRequest) {
        log.info("Stats Server. getStats. Params: statRequest: {};", statRequest);

        statsService.save(statRequest);
    }

}
