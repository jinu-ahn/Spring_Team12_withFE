package com.example.spring_team12_withfe.domain;

import com.example.spring_team12_withfe.dto.request.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.request.ReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Review extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String review;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    public Review(Book_ReviewRequestDto requestDto){
        this.review = requestDto.getReview();
    }
    public void update(ReviewRequestDto requestDto){
        this.review = requestDto.getReview();
    }

}
