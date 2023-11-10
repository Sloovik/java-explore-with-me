package ru.practicum.ewm.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
public class LocationDto {

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

}
