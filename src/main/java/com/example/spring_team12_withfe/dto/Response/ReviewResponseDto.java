package com.example.spring_team12_withfe.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResponseDto {
    private Long id;
    private String review;
    private String user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
