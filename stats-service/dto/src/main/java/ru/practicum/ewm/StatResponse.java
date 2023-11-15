package ru.practicum.ewm;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class StatResponse {

    private String app;

    private String uri;

    private Long hits;

}
