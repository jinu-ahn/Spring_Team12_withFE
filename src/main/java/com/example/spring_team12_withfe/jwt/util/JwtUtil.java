package com.example.spring_team12_withfe.jwt.util;


import com.example.spring_team12_withfe.domain.RefreshToken;
import com.example.spring_team12_withfe.repository.RefreshTokenRepository;
import com.example.spring_team12_withfe.jwt.dto.TokenDto;
import com.example.spring_team12_withfe.security.user.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Slf4j//로깅에 대한 추상 레이어를 제공하는 인터페이스
//로깅: 시스템 동작 시 시스템 상태/작동 정보를 시간의 경과에 따라 기록하는 것
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    public static final String ACCESS_TOKEN = "Access-Token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
    private static final long ACCESS_TOKEN_TIME = 20 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 30 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    //키랑 알고리즘 타입도 가져옴
    Key key;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //Component로 빈 등록할때 초기화를 해주면 됨
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    //토큰생성
    public TokenDto createAllToken(String username) {
        //토큰을 하나도 만들 수 있고, 한번에 다 만들 수 도 있고..
        return new TokenDto(createToken(username, "Access"), createToken(username, "Refresh"));
    }

    public String createToken(String username, String type) {
        Date date = new Date();

        long time = type.equals("Access") ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;

        //  implementation 'io.jsonwebtoken:jjwt:0.9.1'라이브러리 추가했었으나, 35라인 Key 웹토큰 라이브러리가 안나와서
        //	compileOnly group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.11.2'
        //	runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.11.2'
        //	runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.11.2'
        //  로 라이브러리 변경
        return Jwts.builder()
                .setSubject(username)
                //인자에 date타입으로 들어감
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    //header 토큰 가져오는 기능
    public String getTokenFromHeader(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) : request.getHeader(REFRESH_TOKEN);
    }

    //토큰 검증
    public Boolean tokenValidation(String token) {
        // 반환값을 맞추기 위해 try catch문으로 Exception 처리하기
        try {
            //내부적으로 파싱하면서 위변조, Expired 인지 확인해서 Exception날린다
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    //refresh토큰 검증
    public Boolean refreshTokenValidation(String token) {
        // token 검증, table에 존재하는지 확인, 받아온 refresh token이랑 DB에 있는 토큰이랑 match
      if(!tokenValidation(token)) return false;

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberUsername(getUsernameFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }


    // 인증 객체 생성
    public Authentication getAuthentication(String username) {
        //userDetailsService를 사용해서 Member객체 만들어서
        // Member객체가 들어있는 UserDetailsImple을 userDetails 에 넣어서
        // 인증객체 만드는 토큰에 넣어서 만들어서 반환
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //principal, credential, authorities 순서대로 넣어서 객체를 만듬
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //username가져오는 기능
    public String getUsernameFromToken(String token){
       return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody().getSubject();
    }

}
