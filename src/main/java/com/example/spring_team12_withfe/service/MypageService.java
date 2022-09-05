package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.dto.Response.BookResponseDto;
import com.example.spring_team12_withfe.dto.Response.MypageResponseDto;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.jwt.TokenProvider;
import com.example.spring_team12_withfe.repository.BookRepository;
import com.example.spring_team12_withfe.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    public ResponseDto<?> mypage(HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            throw new NullPointerException("Token이 유효하지 않습니다.");

        List<Book> bookList = bookRepository.findByMemberId(member.getId());

        List<BookResponseDto> fundingList = new ArrayList<>();

        for(Book book : bookList){
            fundingList.add(
            BookResponseDto.builder()
                    .thumbnail(book.getThumbnail())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .build()
            );
        }


        return ResponseDto.success(
                MypageResponseDto.builder()
                        .user(member.getUsername())
                        .fundingList(fundingList)
                        .build()
        );
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
