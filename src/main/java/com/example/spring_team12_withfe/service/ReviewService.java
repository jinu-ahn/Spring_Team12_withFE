package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Book;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.domain.Review;
import com.example.spring_team12_withfe.dto.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.ResponseDto;
import com.example.spring_team12_withfe.dto.ReviewRequestDto;
import com.example.spring_team12_withfe.jwt.JwtFilter;
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
    public void create(Book_ReviewRequestDto requestDto, HttpServletRequest request) {

        Book book = Book.builder()
                .image(requestDto.getImage())
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .publisher(requestDto.getPublisher())
                .build();
        bookRepository.saveAndFlush(book);
        Review review = Review.builder()
                .review(requestDto.getReview())
                .member(new Member())
                .book(book)
                .build();
        reviewRepository.save(review);
    }
    @Transactional
    public void update(Long id,ReviewRequestDto requestDto){
        Review review = isParesentReview(id);
        if(review == null)
            throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
        review.update(requestDto);
    }

    @Transactional
    public void delete(Long id){
        Review review = isParesentReview(id);
        if(review == null)
            throw  new IllegalArgumentException("리뷰가 존재하지 않습니다");
        reviewRepository.deleteById(id);
    }

    @Transactional
    public Review isParesentReview(Long id){
        Optional<Review> optionalPost = reviewRepository.findById(id);
        return optionalPost.orElse(null);
    }


//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!jwtUtil.tokenValidation(request.getHeader("Refresh-Token"))) {
//            return null;
//        }
//        return null;
//    }
}

