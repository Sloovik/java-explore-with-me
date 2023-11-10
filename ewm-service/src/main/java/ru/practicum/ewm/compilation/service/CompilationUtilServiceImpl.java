package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventUtilService;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationUtilServiceImpl implements CompilationUtilService {

    private final CompilationRepository compilationRepository;

    private final EventUtilService eventUtilService;

    @Override
    public Compilation getCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id: %x not found", compilationId)));
    }

    @Override
    public CompilationDto getCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));

        if (compilation.getEvents() != null) {
            List<Event> events = new ArrayList<>(compilation.getEvents());

            compilationDto.setEvents(eventUtilService.getEventShortDtos(events));
        }

        return compilationDto;
    }

}
