package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.repository.UserAdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestPrivateServiceImpl implements RequestPrivateService {

    private final EventRepository eventRepository;
    private final UserAdminRepository userAdminRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        log.info("RequestPrivateServiceImpl getRequests. Params: userId: {};", userId);

        List<ParticipationRequestDto> participationRequestDtos = participationRequestRepository
                .findByRequester(userId).stream().map(ParticipationRequestMapper::toDto).collect(Collectors.toList());

        log.info("RequestPrivateServiceImpl getRequests. participationRequestDtos: {};", participationRequestDtos);

        return participationRequestDtos;
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info("RequestPrivateServiceImpl getRequests. Params: userId: {}; eventId: {};", userId, eventId);

        if (!userAdminRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %x not found", userId));
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id %x not found", eventId)));

        log.info("RequestPrivateServiceImpl getRequests. event: {};", event);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The event initiator cannot submit an application for participation");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("To participate, you must publish the event.");
        }

        if (participationRequestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new ConflictException("You are already a member");
        }

        List<ParticipationRequest> confirmed = participationRequestRepository.findConfirmedRequestsForEvent(eventId);

        log.info("RequestPrivateServiceImpl getRequests. confirmed: {};", confirmed);

        if (event.getParticipantLimit() != 0 && confirmed.size() >= event.getParticipantLimit()) {
            throw new ConflictException("Maximum participant level reached");
        }

        RequestStatus requestStatus = !event.getRequestModeration() || event.getParticipantLimit() == 0
                ? RequestStatus.CONFIRMED
                : RequestStatus.PENDING;

        ParticipationRequestDto participationRequestDto = ParticipationRequestMapper
                .toDto(participationRequestRepository.save(ParticipationRequestMapper.toModel(userId, eventId, requestStatus)));

        log.info("RequestPrivateServiceImpl getRequests. participationRequestDto: {};", participationRequestDto);

        return participationRequestDto;
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        if (!userAdminRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %x not found", userId));
        }

        ParticipationRequest request = participationRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request with id %x not found", requestId)));

        request.setStatus(RequestStatus.CANCELED);

        return ParticipationRequestMapper.toDto(participationRequestRepository.save(request));
    }

}
