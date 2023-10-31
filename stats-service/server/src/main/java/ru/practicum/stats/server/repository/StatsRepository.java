package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.dto.StatResponse;
import ru.practicum.stats.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stat, Long> {

    @Query("SELECT new ru.practicum.stats.dto.StatResponse(s.app, s.uri, COUNT (DISTINCT s.ip)) FROM Stat as s" +
            " WHERE s.created BETWEEN :start AND :end" +
            " GROUP BY s.app, s.uri, s.ip" +
            " ORDER BY COUNT (s.ip) DESC ")
    List<StatResponse> getStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.StatResponse(s.app, s.uri, COUNT (s.ip)) FROM Stat as s" +
            " WHERE s.created BETWEEN :start AND :end" +
            " GROUP BY s.app, s.uri, s.ip" +
            " ORDER BY COUNT (DISTINCT s.ip) DESC ")
    List<StatResponse> getStatsWithUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.StatResponse(s.app, s.uri, COUNT (s.ip)) FROM Stat as s" +
            " WHERE s.created BETWEEN :start AND :end AND s.uri IN (:uris)" +
            " GROUP BY s.app, s.uri, s.ip" +
            " ORDER BY COUNT (s.ip) DESC ")
    List<StatResponse> getStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.StatResponse(s.app, s.uri, COUNT (DISTINCT s.ip)) FROM Stat as s" +
            " WHERE s.created BETWEEN :start AND :end AND s.uri IN (:uris)" +
            " GROUP BY s.app, s.uri, s.ip" +
            " ORDER BY COUNT (DISTINCT s.ip) DESC ")
    List<StatResponse> getStatsWithUrisAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

}
