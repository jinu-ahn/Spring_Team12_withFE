package com.example.spring_team12_withfe.dto.request;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
@Getter
public class Book_ReviewRequestDto {
    private Long id;
    private String thumbnail;
    private String title;
    private String author;
    private String publisher;
    private String review;
}
