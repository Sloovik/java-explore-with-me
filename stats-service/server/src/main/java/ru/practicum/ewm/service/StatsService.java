package ru.practicum.ewm.service;

import ru.practicum.ewm.StatRequest;
import ru.practicum.ewm.StatResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<StatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void save(StatRequest hit);

}
