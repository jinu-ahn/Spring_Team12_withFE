package com.example.spring_team12_withfe.service;

import com.example.spring_team12_withfe.dto.Response.GlobalResDto;
import com.example.spring_team12_withfe.dto.member.LoginReqDto;
import com.example.spring_team12_withfe.dto.member.MemberReqDto;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {
    GlobalResDto signup(MemberReqDto memberReqDto);

    GlobalResDto login(LoginReqDto loginReqDot, HttpServletResponse response);
}