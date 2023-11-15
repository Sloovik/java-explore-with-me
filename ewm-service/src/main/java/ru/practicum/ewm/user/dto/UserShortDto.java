package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
public class UserShortDto {

    private Long id;

    @Size(min = 2, max = 250)
    private String name;

}
