package com.example.spring_team12_withfe.controller;


import com.example.spring_team12_withfe.dto.CommentReqDto;
import com.example.spring_team12_withfe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public String createComment(@RequestBody CommentReqDto commentReqDto){
        commentService.createComment(commentReqDto);
        return "SUCCESS";
    }
}
