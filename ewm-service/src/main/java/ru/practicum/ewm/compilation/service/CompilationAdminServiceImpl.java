package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    private final CompilationUtilService compilationUtilService;

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);

        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        }

        return compilationUtilService.getCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void delete(Long compilationId) {
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto update(Long compilationId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = compilationUtilService.getCompilationById(compilationId);

        if (updateCompilationDto.getEvents() != null) {
            Set<Event> events = updateCompilationDto.getEvents().stream()
                    .map(id -> Event.builder().id(id).build()).collect(Collectors.toSet());

            compilation.setEvents(events);
        }

        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }

        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }

        return compilationUtilService.getCompilationDto(compilationRepository.save(compilation));
    }

}
