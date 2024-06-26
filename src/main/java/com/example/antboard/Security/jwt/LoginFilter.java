package com.example.antboard.Security.jwt;

import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.entity.JwtToken;
import com.example.antboard.repository.JwtTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Component
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(JwtTokenRepository jwtTokenRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        // 지정된 로그인 처리 URL 설정
        setFilterProcessesUrl("/user/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        String email = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String refreshToken = jwtTokenProvider.createJwt("refresh", email, role, 600000L); // 예제를 단순화하기 위해 'role'을 직접 지정했습니다.
        String accessToken = jwtTokenProvider.createJwt("access", email, role, 86400000L);
        jwtTokenRepository.save(new JwtToken(email, refreshToken, accessToken));

        JwtToken jwtToken = new JwtToken(email, refreshToken, accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String jwtTokenJson = objectMapper.writeValueAsString(jwtToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jwtTokenJson);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication Failed: " + failed.getMessage());
    }
}
