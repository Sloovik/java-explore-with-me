package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

public interface CompilationAdminService {

    CompilationDto add(NewCompilationDto newCompilationDto);

    void delete(Long compilationId);

    CompilationDto update(Long compilationId, UpdateCompilationDto newCompilationDto);

}
