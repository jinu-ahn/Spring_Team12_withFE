package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.dto.Response.BookResponseDto;
import com.example.spring_team12_withfe.dto.Response.BooksResponseDto;
import com.example.spring_team12_withfe.dto.Response.NaverBookResponseDto;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.dto.request.BookRequestDto;
import com.example.spring_team12_withfe.repository.BookRepository;
import com.example.spring_team12_withfe.utils.NaverBookSearch;
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
    private final NaverBookSearch naverBookSearch;
    @Transactional
    public BooksResponseDto getbooks(){
        List<Book> bookList = bookRepository.findAll();

        List<BookResponseDto> bookResponseDtoList = new ArrayList<>();

        for(Book book : bookList){
            bookResponseDtoList.add(
                BookResponseDto.builder()
                        .id(book.getId())
                        .thumbnail(book.getThumbnail())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .build()
            );
        }
        return BooksResponseDto.builder()
                .books(bookResponseDtoList)
                .build();
    }

    public NaverBookResponseDto getOpenAPI(String query) {
        String result = naverBookSearch.search(query);
        List<BookRequestDto> naver_book = naverBookSearch.fromJSONtoBooks(result);

        List<BookResponseDto> books = new ArrayList<>();
        for(BookRequestDto book : naver_book){
            books.add(
                    BookResponseDto.builder()
                            .thumbnail(book.getThumbnail())
                            .title(book.getTitle())
                            .author(book.getAuthor())
                            .publisher(book.getPublisher())
                            .build()
            );
        }
        return NaverBookResponseDto.builder()
                .infos(books)
                .build();
    }



//    @Transactional
//    public Book isParesentBook(Long id){
//        Optional<Book> optionalPost = bookRepository.findById(id);
//        return optionalPost.orElse(null);
//    }

}
