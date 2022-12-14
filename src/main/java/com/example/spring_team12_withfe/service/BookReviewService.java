package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Comment;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.domain.BookReview;
import com.example.spring_team12_withfe.dto.response.*;
import com.example.spring_team12_withfe.dto.request.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.request.ReviewRequestDto;
import com.example.spring_team12_withfe.jwt.TokenProvider;
import com.example.spring_team12_withfe.repository.Book_ReviewRepository;
import com.example.spring_team12_withfe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookReviewService {
    private final Book_ReviewRepository book_reviewRepository;

    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public B_ResponseDto<?> getAllbook_review(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<BookReview> bookReviews = book_reviewRepository.findAllByOrderByHeartCntDesc(pageable);
        List<BookReviewResponseDto> book_reviewResponseDto = new ArrayList<>();

        for(BookReview book_review : bookReviews){
            book_reviewResponseDto.add(
                    BookReviewResponseDto.builder()
                            .id(book_review.getId())
                            .username(book_review.getMember().getUsername())
                            .thumbnail(book_review.getThumbnail())
                            .title(book_review.getTitle())
                            .author(book_review.getAuthor())
                            .publisher(book_review.getPublisher())
                            .review(book_review.getReview())
                            .heart(book_review.getHeartCnt())
                            .createdAt(book_review.getCreatedAt())
                            .modifiedAt(book_review.getModifiedAt())
                            .build()
            );
        }
        return B_ResponseDto.success(book_reviewResponseDto);
    }


    // ?????? ???/?????? ??????
    @Transactional
    public ResponseDto<?> getbook_review(Long id){
        BookReview book_review = isParesentReview(id);
        if(book_review == null)
            ResponseDto.fail("NOT_FOUND_REVIEW" ,"????????? ???????????? ????????????.");

        List<Comment> commentList= commentRepository.findAllByBookReview(book_review);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();


        for(Comment comment:commentList){
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .username(comment.getMember().getUsername())
                            .comment(comment.getComment())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }


        return ResponseDto.success(
                BookReviewResponseDto.builder()
                        .id(book_review.getId())
                        .username(book_review.getMember().getUsername())
                        .thumbnail(book_review.getThumbnail())
                        .title(book_review.getTitle())
                        .author(book_review.getAuthor())
                        .publisher(book_review.getPublisher())
                        .review(book_review.getReview())
                        .comments(commentResponseDtoList)
                        .heart(book_review.getHeartCnt())
                        .createdAt(book_review.getCreatedAt())
                        .modifiedAt(book_review.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> create(Book_ReviewRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);

        if (member == null) {
            throw new NullPointerException("Token??? ???????????? ????????????.");
        }



        BookReview book_review = BookReview.builder()
                .thumbnail(requestDto.getThumbnail())
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .publisher(requestDto.getPublisher())
                .review(requestDto.getReview())
                .heartCnt(0L)
                .member(member)
                .build();


        // ?????? ID, ??? ??????, ??? ????????? ????????????
        List<BookReview> bookReview = book_reviewRepository.findByMemberIdAndTitleAndAuthor(member.getId(),requestDto.getTitle(), requestDto.getAuthor());

        // ?????? ???/????????? ?????? ?????? ??????
        if(bookReview.size() == 1)
            return ResponseDto.fail("ALREADY_REVIEW","?????? ????????? ?????????????????????.");

        book_reviewRepository.save(book_review);

        return ResponseDto.success(
                BookReviewResponseDto.builder()
                        .id(book_review.getId())
                        .thumbnail(book_review.getThumbnail())
                        .title(book_review.getTitle())
                        .author(book_review.getAuthor())
                        .publisher(book_review.getPublisher())
                        .username(book_review.getMember().getUsername())
                        .review(book_review.getReview())
                        .heart(book_review.getHeartCnt())
                        .createdAt(book_review.getCreatedAt())
                        .modifiedAt(book_review.getModifiedAt())
                        .build()
        );

    }

    @Transactional
    public ResponseDto<?> update(Long id,ReviewRequestDto requestDto,HttpServletRequest request){
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token??? ???????????? ????????????.");
        }
        BookReview book_review = isParesentReview(id);
        if(book_review == null)
            return ResponseDto.fail("NOT_FOUND_REVIEW", "????????? ???????????? ????????????.");

        if(book_review.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST","???????????? ????????? ??? ????????????.");
        }
        book_review.update(requestDto);
        return ResponseDto.success(
                ReviewResponseDto.builder()
                        .id(book_review.getId())
                        .review(requestDto.getReview())
                        .username(book_review.getMember().getUsername())
                        .createdAt(book_review.getCreatedAt())
                        .modifiedAt(book_review.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> delete(Long id,HttpServletRequest request){
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token??? ???????????? ????????????.");
        }

        BookReview book_review = isParesentReview(id);
        if(book_review == null)
            return ResponseDto.fail("NOT_FOUND_REVIEW", "????????? ???????????? ????????????.");

        if(book_review.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST","???????????? ????????? ??? ????????????.");
        }

        book_reviewRepository.deleteById(id);
        return ResponseDto.success("");
    }


    @Transactional
    public BookReview isParesentReview(Long id){
        Optional<BookReview> optionalPost = book_reviewRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}

