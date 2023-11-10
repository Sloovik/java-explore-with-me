package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.event.enums.EventRequestStatusUpdate;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UpdateEventRequestStatusDto {

    private List<Long> requestIds;

    private EventRequestStatusUpdate status;

}
