package ru.practicum.ewm.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 5000)
    private String text;

}
