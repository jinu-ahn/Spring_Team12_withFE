package com.example.spring_team12_withfe.controller;
import com.example.spring_team12_withfe.dto.Book_ReviewRequestDto;
import com.example.spring_team12_withfe.dto.ReviewRequestDto;
import com.example.spring_team12_withfe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/review/")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/auth")
    public String createReview(@RequestBody Book_ReviewRequestDto requestDto){
        reviewService.create(requestDto);
        return "SUCCESS";
    }

    @PutMapping("/auth/{id}")
    public String update(@PathVariable Long id, @RequestBody ReviewRequestDto requestDto){
        reviewService.update(id,requestDto);
        return "SUCCESS";
    }
    @DeleteMapping("/auth/{id}")
    public String update(@PathVariable Long id){
        reviewService.delete(id);
        return "SUCCESS";
    }

}

