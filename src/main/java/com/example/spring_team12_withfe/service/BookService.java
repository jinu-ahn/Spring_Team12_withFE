package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.domain.Review;
import com.example.spring_team12_withfe.dto.BookRequestDto;
import com.example.spring_team12_withfe.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public List<Book> getbooks(){
        return bookRepository.findAll();
    }




    @Transactional
    public Book isParesentBook(Long id){
        Optional<Book> optionalPost = bookRepository.findById(id);
        return optionalPost.orElse(null);
    }

}
