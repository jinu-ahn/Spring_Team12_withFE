package com.example.spring_team12_withfe.security;

import com.example.spring_team12_withfe.jwt.Filter.JwtAuthFilter;
import com.example.spring_team12_withfe.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityconfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Value("${jwt.secret.key}")
    String Secret_key;

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //webSecuirty로 ignore해주기
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //멤버로 넘어오는 기본적인거는 다 풀어준다(로그인관련된거)
                .and().authorizeRequests().antMatchers("/api/member/**").permitAll()
//                .anyRequest().authenticated() // 여기서 시큐리티가 403 으로 막음
                //jwt관련
                        .and().addFilterBefore(new JwtAuthFilter(jwtUtil,Secret_key), UsernamePasswordAuthenticationFilter.class);
       return http.build();
    }
}
