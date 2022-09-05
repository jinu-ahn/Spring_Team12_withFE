package com.example.spring_team12_withfe.controller;
import com.example.spring_team12_withfe.dto.request.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.request.ReviewRequestDto;
import com.example.spring_team12_withfe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/review")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/auth")
    public String createReview(@RequestBody Book_ReviewRequestDto requestDto, HttpServletRequest request){
        reviewService.create(requestDto,request);
        return "SUCCESS";
    }


    @PutMapping("/review/{id}")
    public String update(@PathVariable Long id, @RequestBody ReviewRequestDto requestDto){
        reviewService.update(id,requestDto);
        return "SUCCESS";
    }
    @DeleteMapping("/review/{id}")
    public String update(@PathVariable Long id){
        reviewService.delete(id);
        return "SUCCESS";
    }

}

