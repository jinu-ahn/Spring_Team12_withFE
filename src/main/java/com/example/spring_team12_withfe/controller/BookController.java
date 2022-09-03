package com.example.spring_team12_withfe.controller;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.dto.BookRequestDto;

import com.example.spring_team12_withfe.service.BookService;
import com.example.spring_team12_withfe.utils.NaverBookSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;
    private final NaverBookSearch naverBookSearch;

    @GetMapping("/api/books")
    public List<Book> readBooks() {
        return bookService.getbooks();
    }

    @GetMapping("/api/books/search")
    public List<BookRequestDto> getBooks(@RequestParam String query) {
        String result = naverBookSearch.search(query);
        return naverBookSearch.fromJSONtoBooks(result);
    }



}
