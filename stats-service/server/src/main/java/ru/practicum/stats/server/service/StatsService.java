package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.StatRequest;
import ru.practicum.stats.dto.StatResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<StatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void save(StatRequest hit);

}
