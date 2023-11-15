package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class SearchAdminEventsParamsDto {

    private List<Long> users;

    private List<EventState> states;

    private List<Long> categories;

    private LocalDateTime start;

    private LocalDateTime end;

    private Pageable pageable;

}
