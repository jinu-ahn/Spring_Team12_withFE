package com.example.spring_team12_withfe.repository;

import com.example.spring_team12_withfe.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
