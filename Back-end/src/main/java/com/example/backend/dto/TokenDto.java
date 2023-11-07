package com.example.backend.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class TokenDto {

    private String grantType;
    private String acessToken;
    private String refreshToken;
}
