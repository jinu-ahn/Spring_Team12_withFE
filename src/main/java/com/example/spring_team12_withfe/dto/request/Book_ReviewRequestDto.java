package com.example.spring_team12_withfe.dto.request;


import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class Book_ReviewRequestDto {
    private String thumbnail;
    private String title;
    private String author;
    private String publisher;
    private String review;

}
