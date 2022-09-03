package com.example.spring_team12_withfe.domain;

import com.example.spring_team12_withfe.dto.Book_ReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name="books")
public class Book {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    public Book(Book_ReviewRequestDto requestDto){
        this.image = requestDto.getImage();
        this.title = requestDto.getTitle();
        this.author = requestDto.getAuthor();
        this.publisher = requestDto.getPublisher();
    }
}
