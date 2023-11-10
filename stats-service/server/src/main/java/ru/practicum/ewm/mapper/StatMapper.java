package ru.practicum.ewm.mapper;

import ru.practicum.ewm.StatRequest;
import ru.practicum.ewm.model.Stat;

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
