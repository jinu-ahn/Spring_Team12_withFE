package com.example.spring_team12_withfe.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookResponseDto {
    private String thumbnail;
    private String title;
    private String author;
    private String publisher;
}
