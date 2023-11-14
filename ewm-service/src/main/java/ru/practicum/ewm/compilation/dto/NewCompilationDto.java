package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {

    private Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    private List<Long> events;

}
