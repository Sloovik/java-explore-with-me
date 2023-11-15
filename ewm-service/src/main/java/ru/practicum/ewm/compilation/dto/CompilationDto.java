package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@Builder
public class CompilationDto {

    private Long id;

    @Builder.Default
    private Boolean pinned = false;

    private String title;

    private List<EventShortDto> events;

}
