package com.example.spring_team12_withfe.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class Book_ReviewResponseDto {
    private String thumbnail;
    private String title;
    private String author;
    private String publisher;
    private String user;
    private String review;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
