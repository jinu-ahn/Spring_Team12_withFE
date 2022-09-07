package com.example.spring_team12_withfe.service;


import com.example.spring_team12_withfe.domain.BookReview;
import com.example.spring_team12_withfe.dto.response.ResponseDto;
import com.example.spring_team12_withfe.domain.Heart;
import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.jwt.TokenProvider;
import com.example.spring_team12_withfe.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeartService {
    private final HeartRepository heartRepository;
    
    private final Book_ReviewService book_reviewService;
    private final TokenProvider tokenProvider;

    // 좋아요 + 좋아요 해제 둘다 가능
    @Transactional
    public ResponseDto<?> heart(Long reviewId, HttpServletRequest request) throws IOException {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        BookReview book_review = book_reviewService.isParesentReview(reviewId);
        if (null == book_review) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 리뷰 id 입니다.");
        }
//        // 동일한 리뷰에 동일한 계정으로 이미 좋아요한 내역이 있을 경우 -> 좋아요 못하게..
//        if (heartRepository.findHeartByMemberAndReview(heartDto.getReviewId(), heartDto.getMemberId()).isPresent())
//            throw new IllegalArgumentException("ALREADY_HEARTED");

        List<Heart> reviewHeart = heartRepository.findByMemberIdAndReviewId(member.getId(), reviewId);
        boolean check = false;//안좋아요
        for (Heart heart : reviewHeart ) {
            if (heart.getMember().equals(member)) {//이미 해당 유저가 좋아요 했을 경우
                check = true; //좋아요
                System.out.println("이미 좋아요 한 게시물 입니다.");
                heartRepository.delete(heart);//좋아요 해제
                break;
            }
        }
            if (check != true) { // 안좋아요에서 POST할 경우 좋아요로 변경
                System.out.println("좋아요");
                Heart heart = Heart.builder()
                        .member(member)
                        .review(book_review)
                        .build();
                heartRepository.save(heart);// 좋아요 저장
            }
        return ResponseDto.success("좋아요 버튼이 작동됐습니다");
        }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
