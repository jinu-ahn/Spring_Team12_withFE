package com.example.spring_team12_withfe.controller;
import com.example.spring_team12_withfe.dto.Response.ResponseDto;
import com.example.spring_team12_withfe.dto.request.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.request.ReviewRequestDto;
import com.example.spring_team12_withfe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ReviewController {
    private final ReviewService reviewService;


    @GetMapping("/review/{id}")
    public ResponseDto<?> getreview(@PathVariable Long id) {
        return reviewService.getreview(id);
    }

    @PostMapping("/auth/review")
    public ResponseDto<?> createReview(@RequestBody Book_ReviewRequestDto requestDto, HttpServletRequest request){
        return reviewService.create(requestDto,request);
    }


    @PutMapping("/auth/review/{id}")
    public ResponseDto<?> update(@PathVariable Long id, @RequestBody ReviewRequestDto requestDto){
        return reviewService.update(id,requestDto);
    }
    @DeleteMapping("/auth/review/{id}")
    public ResponseDto<?> delete(@PathVariable Long id){
        return reviewService.delete(id);
    }

}

