package com.example.spring_team12_withfe.controller;

import com.example.spring_team12_withfe.dto.GlobalResDto;
import com.example.spring_team12_withfe.dto.member.LoginReqDto;
import com.example.spring_team12_withfe.dto.member.MemberReqDto;
import com.example.spring_team12_withfe.jwt.util.JwtUtil;
import com.example.spring_team12_withfe.security.user.UserDetailsImpl;
import com.example.spring_team12_withfe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/member/signup")
    public GlobalResDto signup(@RequestBody @Valid MemberReqDto memberReqDto) {
        return memberService.signup(memberReqDto);
    }

    @PostMapping("/member/login")
    public GlobalResDto login(@RequestBody @Valid LoginReqDto loginReqDot, HttpServletResponse response) {
        return memberService.login(loginReqDot, response);
    }

    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        //refresh토큰을 주면서 access토큰을 발급하는 API
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getMember().getUsername(), "Access"));
        return new GlobalResDto("issuedToken Success", HttpStatus.OK.value());
    }

}