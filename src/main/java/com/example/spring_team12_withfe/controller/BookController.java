package com.example.spring_team12_withfe.controller;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.dto.Response.BooksResponseDto;
import com.example.spring_team12_withfe.dto.Response.NaverBookResponseDto;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.dto.request.BookRequestDto;

import com.example.spring_team12_withfe.service.BookService;
import com.example.spring_team12_withfe.utils.NaverBookSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public BooksResponseDto readBooks() {
        return bookService.getbooks();
    }

    @GetMapping("/api/books/search")
    public NaverBookResponseDto getBooks(@RequestParam String query) {
        return bookService.getOpenAPI(query);
    }



}
