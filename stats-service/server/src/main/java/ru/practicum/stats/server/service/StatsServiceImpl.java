package ru.practicum.stats.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.StatRequest;
import ru.practicum.stats.dto.StatResponse;
import ru.practicum.stats.server.mapper.StatMapper;
import ru.practicum.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    public List<StatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris.isEmpty()) {
            if (unique) {
                return statsRepository.getStatsWithUnique(start, end);
            } else {
                return statsRepository.getStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.getStatsWithUrisAndUnique(start, end, uris);
            } else {
                return statsRepository.getStatsWithUris(start, end, uris);
            }
        }
    }

    public void save(StatRequest statRequest) {
        statsRepository.save(StatMapper.toStat(statRequest));
    }

}
