package com.example.spring_team12_withfe.jwt.Filter;


import com.example.spring_team12_withfe.dto.GlobalResDto;
import com.example.spring_team12_withfe.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//필터는 Componenet로 등록할 필요가 없음
public class JwtAuthFilter extends OncePerRequestFilter {

    //jwt관련된거 만들어서 쓰면 될것 같다
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //토큰값 받아오기
        String accessToken = jwtUtil.getTokenFromHeader(request, "Access");
        String refreshToken = jwtUtil.getTokenFromHeader(request, "Refresh");

        if (accessToken != null) {
            if (!jwtUtil.tokenValidation(accessToken)) {
                jwtExceptionHandler(response, "AccessToken Expired", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(jwtUtil.getUsernameFromToken(accessToken));
        } else if (refreshToken != null) {
            if (!jwtUtil.refreshTokenValidation(refreshToken)) {
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(jwtUtil.getUsernameFromToken(refreshToken));
        }

        filterChain.doFilter(request, response);

    }
    //지금 사용자는 인증이 된 사용자다
    public void setAuthentication(String username) {
        Authentication authentication = jwtUtil.getAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}