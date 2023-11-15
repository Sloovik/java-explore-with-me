package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
public class UserDto {

    private Long id;

    @Size(min = 2, max = 250)
    private String name;

    @Email
    @Size(min = 6, max = 254)
    private String email;

}
