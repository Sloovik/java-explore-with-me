package ru.practicum.ewm.location.service;

import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.model.Location;

import java.util.Optional;

public interface LocationService {

    Optional<LocationDto> get(double lat, double lon);

    Location getWithSave(LocationDto locationDto);

}
