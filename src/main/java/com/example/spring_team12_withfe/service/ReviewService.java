package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.domain.Review;
import com.example.spring_team12_withfe.dto.Response.Book_ReviewResponseDto;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.dto.Response.ReviewResponseDto;
import com.example.spring_team12_withfe.dto.request.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.request.ReviewRequestDto;
import com.example.spring_team12_withfe.repository.BookRepository;
import com.example.spring_team12_withfe.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ResponseDto<?> create(Book_ReviewRequestDto requestDto, HttpServletRequest request) {
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }

        Book book = Book.builder()
                .thumbnail(requestDto.getThumbnail())
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .publisher(requestDto.getPublisher())
//                .member(member)
                .build();
        bookRepository.saveAndFlush(book);

        Review review = Review.builder()
                .review(requestDto.getReview())
                .member(new Member())
                .book(book)
//                .member(member)
                .build();
        reviewRepository.save(review);

        return ResponseDto.success(
                Book_ReviewResponseDto.builder()
                        .thumbnail(book.getThumbnail())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .user(review.getMember().getUsername())
                        .review(review.getReview())
                        .createdAt(review.getCreatedAt())
                        .modifiedAt(review.getModifiedAt())
                        .build()
        );

    }
    @Transactional
    public ResponseDto<?> update(Long id,ReviewRequestDto requestDto){
        Review review = isParesentReview(id);
        if(review == null)
            throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
        return ResponseDto.success(
                ReviewResponseDto.builder()
                        .id(review.getId())
                        .review(review.getReview())
                        .user(review.getMember().getUsername())
                        .createdAt(review.getCreatedAt())
                        .modifiedAt(review.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> delete(Long id){
        Review review = isParesentReview(id);
        if(review == null)
            throw  new IllegalArgumentException("리뷰가 존재하지 않습니다");
        reviewRepository.deleteById(id);
        return ResponseDto.success("");
    }

    @Transactional
    public Review isParesentReview(Long id){
        Optional<Review> optionalPost = reviewRepository.findById(id);
        return optionalPost.orElse(null);
    }


//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!jwtUtil.refreshTokenValidation(request.getHeader("Refresh-Token"))) {
//            return null;
//        }
//        return jwtUtil.getMemberFromAuthentication();
//    }

}

