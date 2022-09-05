package com.example.spring_team12_withfe.service.impl;

import com.example.spring_team12_withfe.domain.Member;
import com.example.spring_team12_withfe.domain.RefreshToken;
import com.example.spring_team12_withfe.dto.Response.GlobalResDto;
import com.example.spring_team12_withfe.repository.RefreshTokenRepository;
import com.example.spring_team12_withfe.dto.member.LoginReqDto;
import com.example.spring_team12_withfe.dto.member.MemberReqDto;
import com.example.spring_team12_withfe.jwt.dto.TokenDto;
import com.example.spring_team12_withfe.jwt.util.JwtUtil;
import com.example.spring_team12_withfe.repository.MemberRepository;
import com.example.spring_team12_withfe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public GlobalResDto signup(MemberReqDto memberReqDto) {
        // username 중복 검사
        if (memberRepository.findByUsername(memberReqDto.getUsername()).isPresent()) {
            throw new RuntimeException("SignUp Fail Cause Overlap");
        }

        memberReqDto.setPassword(passwordEncoder.encode(memberReqDto.getPassword()));
        Member member = new Member(memberReqDto);
        memberRepository.save(member);

        return new GlobalResDto("Signup Success", HttpStatus.OK.value());
    }

    @Override
    @Transactional
    public GlobalResDto login(LoginReqDto loginReqDot, HttpServletResponse response) {

        Member member = memberRepository.findByUsername(loginReqDot.getUsername()).orElseThrow(
                ()->new RuntimeException("Not Find Username")
        );

        if(!passwordEncoder.matches(loginReqDot.getPassword(), member.getPassword())){
            throw new RuntimeException("Not Match Password");
        }

        // 토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(member.getUsername());
        //refresh토큰이 이미 만들어져 중복된게 있을 경우
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberUsername(member.getUsername());

        if(refreshToken.isPresent()) {
            refreshToken.get().setRefreshToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken.get());
        }else{
            RefreshToken newRefreshToken = new RefreshToken(tokenDto.getRefreshToken(), member.getUsername());
            refreshTokenRepository.save(newRefreshToken);
        }

        setTokenOnHeader(response, tokenDto);

        return new GlobalResDto("Login Success", HttpStatus.OK.value());
    }

    private void setTokenOnHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }


}