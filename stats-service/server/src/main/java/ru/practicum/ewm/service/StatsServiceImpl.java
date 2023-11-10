package ru.practicum.ewm.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatRequest;
import ru.practicum.ewm.StatResponse;
import ru.practicum.ewm.repository.StatsRepository;
import ru.practicum.ewm.mapper.StatMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    public List<StatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Stats Server Service. getStats. Params: start: {}; end: {}; uris: {}; unique: {};", start, end, uris, unique);

        if (uris.isEmpty()) {
            if (unique) {
                return statsRepository.getStatsWithUnique(start, end);
            }

            return statsRepository.getStats(start, end);
        } else {
            if (unique) {
                return statsRepository.getStatsWithUrisAndUnique(start, end, uris);
            }

            return statsRepository.getStatsWithUris(start, end, uris);
        }
    }

    public void save(StatRequest statRequest) {
        log.info("Stats Server Service. save. Params: statRequest: {};", statRequest);
        statsRepository.save(StatMapper.toStat(statRequest));
    }

}
