package com.example.spring_team12_withfe.dto.member;


import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginReqDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}