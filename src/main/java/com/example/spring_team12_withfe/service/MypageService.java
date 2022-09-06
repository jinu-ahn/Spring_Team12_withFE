package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.dto.Response.BookResponseDto;
import com.example.spring_team12_withfe.dto.Response.MypageResponseDto;
import com.example.spring_team12_withfe.jwt.TokenProvider;
import com.example.spring_team12_withfe.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final BookRepository bookRepository;
    private final TokenProvider tokenProvider;


    public MypageResponseDto mypage(HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            throw new NullPointerException("Token이 유효하지 않습니다.");

        List<Book> bookList = bookRepository.findByMemberId(member.getId());

        List<BookResponseDto> book_List = new ArrayList<>();

        for(Book book : bookList){
            book_List.add(
            BookResponseDto.builder()
                    .id(book.getId())
                    .thumbnail(book.getThumbnail())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .build()
            );
        }


        return MypageResponseDto.builder()
                        .username(member.getUsername())
                        .Book_List(book_List)
                        .build();
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
