package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;

public interface CompilationUtilService {

    Compilation getCompilationById(Long compilationId);

    CompilationDto getCompilationDto(Compilation compilation);

}
