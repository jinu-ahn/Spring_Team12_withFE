package com.example.spring_team12_withfe.repository;

import com.example.spring_team12_withfe.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findByMemberId(Long id);
}
