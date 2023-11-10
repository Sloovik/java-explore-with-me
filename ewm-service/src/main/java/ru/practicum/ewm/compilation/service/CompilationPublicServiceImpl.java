package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private final CompilationRepository compilationRepository;

    private final CompilationUtilService compilationUtilService;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        List<CompilationDto> result = new ArrayList<>();

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);

            for (Compilation compilation : compilations) {
                result.add(compilationUtilService.getCompilationDto(compilation));
            }
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();

            for (Compilation compilation : compilations) {
                result.add(compilationUtilService.getCompilationDto(compilation));
            }
        }

        return result;
    }

    @Override
    public CompilationDto getCompilation(Long compilationId) {
        Compilation compilation = compilationUtilService.getCompilationById(compilationId);

        return compilationUtilService.getCompilationDto(compilation);
    }

}
