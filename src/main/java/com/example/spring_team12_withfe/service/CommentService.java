package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.domain.Comment;
import com.example.spring_team12_withfe.dto.CommentReqDto;
import com.example.spring_team12_withfe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    @Transactional
    public void createComment(CommentReqDto commentReqDto) {
        Comment comment = new Comment(commentReqDto);
        commentRepository.save(comment);
    }
}
