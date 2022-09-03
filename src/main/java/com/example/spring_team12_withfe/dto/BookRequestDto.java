package com.example.spring_team12_withfe.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class BookRequestDto {
    private String image;
    private String title;
    private String author;
    private String publisher;


    public BookRequestDto(JSONObject book){
        this.image = book.getString("image");
        this.title = book.getString("title");
        this.author = book.getString("author");
        this.publisher = book.getString("publisher");
    }
}
