package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserAdminMapper;
import ru.practicum.ewm.user.repository.UserAdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserAdminRepository userAdminRepository;

    @Override
    public List<UserDto> findAll(List<Long> ids, Pageable pageable) {
        log.info("Service findAll. Params: ids: {}; pageable: {};", ids, pageable);

        if (ids.isEmpty()) {
            return userAdminRepository.findAll(pageable).stream().map(UserAdminMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        return userAdminRepository.findUsersByIdIn(ids, pageable).stream().map(UserAdminMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto add(UserDto userDto) {
        log.info("Service add. Params: userDto: {};", userDto);
        checkUserExists(userDto);

        UserDto newUser = UserAdminMapper.toUserDto(userAdminRepository.save(UserAdminMapper.toUser(userDto)));

        log.info("Service add. newUser: {};", newUser);

        return newUser;
    }

    @Override
    public void delete(Long userId) {
        log.info("Service add. Params: userId: {};", userId);

        userAdminRepository.deleteById(userId);
    }

    private void checkUserExists(UserDto userDto) {
        if (userAdminRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException(String.format("User with email %s already exits", userDto.getEmail()));
        }
    }

}
