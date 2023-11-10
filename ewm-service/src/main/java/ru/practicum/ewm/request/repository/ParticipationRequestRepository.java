package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEvent(Long eventId);

    List<ParticipationRequest> findByRequester(Long requester);

    boolean existsByRequesterAndEvent(Long userId, Long eventId);

    @Query("SELECT pr " +
            "FROM ParticipationRequest pr " +
            "WHERE pr.id = :requestId " +
            "AND pr.status = 'PENDING' ")
    Optional<ParticipationRequest> findPendingRequestsById(Long requestId);

    @Query("SELECT pr " +
            "FROM ParticipationRequest pr " +
            "WHERE pr.event = :eventId " +
            "AND pr.status = 'CONFIRMED' ")
    List<ParticipationRequest> findConfirmedRequestsForEvent(Long eventId);

    @Query("SELECT pr " +
            "FROM ParticipationRequest pr " +
            "WHERE pr.event IN (:eventIds) " +
            "AND pr.status = 'CONFIRMED' ")
    List<ParticipationRequest> findConfirmedRequestsForEventList(List<Long> eventIds);

}
