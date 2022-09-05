package com.example.spring_team12_withfe.dto.Response;

import com.example.spring_team12_withfe.domain.Book;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MypageResponseDto {
    private String user;
    private List<BookResponseDto> fundingList;
}
