package com.example.spring_team12_withfe.repository;

import com.example.spring_team12_withfe.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
