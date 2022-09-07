package com.example.spring_team12_withfe.dto;


import com.example.spring_team12_withfe.domain.Review;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeartDto {
    private Long reviewId;
    private Long memberId;
}