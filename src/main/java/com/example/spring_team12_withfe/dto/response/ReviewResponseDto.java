package com.example.spring_team12_withfe.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResponseDto {
    private Long id;
    private String review;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
