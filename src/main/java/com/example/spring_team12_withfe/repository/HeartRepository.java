package com.example.spring_team12_withfe.repository;

import com.example.spring_team12_withfe.domain.Heart;
import com.example.spring_team12_withfe.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    // 동일한 리뷰에 동일한 계정으로 이미 좋아요한 내역이 있는지 찾을 때 사용할 메소드
    List<Heart> findByMemberIdAndReviewId(Long member, Long review);
    // 특정 리뷰에 좋아요가 총 몇개 인지 셀때 사용할 메소드
    int countByReview(Review review);
}