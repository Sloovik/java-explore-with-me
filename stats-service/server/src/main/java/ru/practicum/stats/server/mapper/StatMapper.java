package ru.practicum.stats.server.mapper;

import ru.practicum.stats.dto.StatRequest;
import ru.practicum.stats.server.model.Stat;

public class StatMapper {

    public static Stat toStat(StatRequest statRequest) {
        return Stat.builder()
                .ip(statRequest.getIp())
                .uri(statRequest.getUri())
                .app(statRequest.getApp())
                .created(statRequest.getTimestamp())
                .build();
    }

}
