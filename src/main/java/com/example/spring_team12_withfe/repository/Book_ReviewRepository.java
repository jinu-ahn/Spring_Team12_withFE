package com.example.spring_team12_withfe.repository;

import com.example.spring_team12_withfe.domain.BookReview;
import com.example.spring_team12_withfe.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface Book_ReviewRepository extends JpaRepository<BookReview,Long> {
    Optional<BookReview> findById(Long id);
    List<BookReview> findByMemberId(Long member_id);

}
