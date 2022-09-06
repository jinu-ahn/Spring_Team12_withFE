package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.domain.Review;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.domain.Comment;
import com.example.spring_team12_withfe.dto.request.CommentReqDto;
import com.example.spring_team12_withfe.dto.response.CommentResponseDto;
import com.example.spring_team12_withfe.jwt.TokenProvider;
import com.example.spring_team12_withfe.repository.CommentRepository;
import com.example.spring_team12_withfe.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {


    private final ReviewService reviewService;
    private final CommentRepository commentRepository;

    private final ReviewRepository reviewRepository;
    private final TokenProvider tokenProvider;

    //댓글 등록하기
    @Transactional
    public ResponseDto<?> createComment(CommentReqDto commentReqDto, HttpServletRequest request) {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Review review = reviewService.isParesentReview(commentReqDto.getReviewId());
        if (null == review) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 리뷰 id 입니다.");
        }
        Comment comment = Comment.builder()
                .member(member)
                .review(review)
                .comment(commentReqDto.getComment())
                .build();
        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(member.getUsername())
                        .content(comment.getComment())
                        .build()
        );
    }

    //댓글 보여주기
    public ResponseDto<?> showComment() {
        List<Comment> commentList = commentRepository.findAll();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            //좋아요 개수 추가
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getUsername())
                            .content(comment.getComment())
                            .createdAt(comment.getMember().getCreatedAt())
                            .modifiedAt(comment.getReview().getModifiedAt())
                            .build()

            );
        }
        return ResponseDto.success(commentResponseDtoList);
    }


    //댓글 수정하기
    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentReqDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        Review review = reviewService.isParesentReview(requestDto.getReviewId());
        if (review == null) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 리뷰 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }
        comment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getUsername())
                        .content(comment.getComment())
                        .createdAt(comment.getReview().getCreatedAt())
                        .build());
    }

    //댓글 삭제하기

    public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
        return ResponseDto.success("success");
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalPost = commentRepository.findById(id);
        return optionalPost.orElse(null);
    }

}

