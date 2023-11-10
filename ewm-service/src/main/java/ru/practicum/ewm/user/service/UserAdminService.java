package ru.practicum.ewm.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserAdminService {

    List<UserDto> findAll(List<Long> ids, Pageable pageable);

    UserDto add(UserDto userDto);

    void delete(Long userId);

}
