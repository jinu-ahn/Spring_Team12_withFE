package com.example.spring_team12_withfe.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BooksResponseDto {
    List<BookResponseDto> books;
}
