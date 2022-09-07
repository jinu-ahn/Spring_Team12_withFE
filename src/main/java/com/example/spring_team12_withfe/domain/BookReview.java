package com.example.spring_team12_withfe.domain;

import com.example.spring_team12_withfe.dto.request.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.request.ReviewRequestDto;
import lombok.*;


import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BookReview extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column
    private String review;

    @Column
    private Long heart = 0L;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @OneToMany(mappedBy = "bookReview" , cascade = CascadeType.REMOVE)
    private List<Comment> comment;

//    @OneToMany(mappedBy = "bookReview" , cascade = CascadeType.REMOVE)
//    private List<Heart> heart;

    public BookReview(Book_ReviewRequestDto requestDto){
        this.review = requestDto.getReview();
    }
    public void update(ReviewRequestDto requestDto){
        this.review = requestDto.getReview();
    }
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

}
