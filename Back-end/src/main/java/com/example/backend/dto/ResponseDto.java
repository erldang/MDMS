package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@ToString
public class ResponseDto<D> {
    private final D data;
    private final String message;
    private final String ok;

}
