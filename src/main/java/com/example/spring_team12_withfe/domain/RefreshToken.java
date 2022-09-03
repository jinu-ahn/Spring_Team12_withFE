package com.example.spring_team12_withfe.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String memberUsername;

    public RefreshToken() {}

    public RefreshToken(String token, String username) {
        this.refreshToken = token;
        this.memberUsername = username;
    }
}