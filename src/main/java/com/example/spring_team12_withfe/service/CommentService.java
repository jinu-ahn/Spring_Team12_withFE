package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.BookReview;
import com.example.spring_team12_withfe.domain.Comment;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.dto.CommentReqDto;
import com.example.spring_team12_withfe.dto.Response.CommentResponseDto;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.jwt.TokenProvider;
import com.example.spring_team12_withfe.repository.Book_ReviewRepository;
import com.example.spring_team12_withfe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final Book_ReviewRepository book_reviewRepository;

    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;
    @Transactional
    public ResponseDto<?> createComment(CommentReqDto commentReqDto,HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");


        BookReview book_review = isPresentBook_review(commentReqDto.getReviewId());

        if(book_review == null)
            return ResponseDto.fail("NOT_FOUND_REVIEW" , "리뷰를 찾을 수 없습니다.");

        Comment comment = Comment.builder()
                            .member(member)
                            .bookReview(book_review)
                            .comment(commentReqDto.getComment())
                            .build();
        commentRepository.save(comment);


        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .username(member.getUsername())
                        .comment(comment.getComment())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public BookReview isPresentBook_review(Long id) {
        Optional<BookReview> optionalBook_review = book_reviewRepository.findById(id);
        return optionalBook_review.orElse(null);
    }
}
