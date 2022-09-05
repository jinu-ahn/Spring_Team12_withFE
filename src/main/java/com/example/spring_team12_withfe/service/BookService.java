package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.dto.Response.BookResponseDto;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public ResponseDto<?> getbooks(){
        List<Book> bookList = bookRepository.findAll();
        List<BookResponseDto> bookResponseDtoList = new ArrayList<>();

        for(Book book : bookList){
            bookResponseDtoList.add(
                BookResponseDto.builder()
                        .thumbnail(book.getThumbnail())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .build()
            );
        }
        return ResponseDto.success(bookResponseDtoList);
    }




//    @Transactional
//    public Book isParesentBook(Long id){
//        Optional<Book> optionalPost = bookRepository.findById(id);
//        return optionalPost.orElse(null);
//    }

}
