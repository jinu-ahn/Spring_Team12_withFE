package com.example.spring_team12_withfe.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
