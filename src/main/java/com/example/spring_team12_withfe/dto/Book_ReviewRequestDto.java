package com.example.spring_team12_withfe.dto;

import com.example.spring_team12_withfe.domain.Member;
import lombok.Getter;

@Getter
public class Book_ReviewRequestDto {
    private String image;
    private String title;
    private String author;
    private String review;
    private String publisher;

}