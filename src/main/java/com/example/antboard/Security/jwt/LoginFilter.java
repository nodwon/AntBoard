package com.example.antboard.Security.jwt;

import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.entity.RefreshToken;
import com.example.antboard.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginFilter(RefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        // 지정된 로그인 처리 URL 설정
        setFilterProcessesUrl("/user/login");
    }


    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("Attempt authentication...");
        LoginDto loginDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = objectMapper.readValue(messageBody, LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        return this.getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        String accessToken = jwtTokenProvider.createJwt("access", username, "ROLE_USER", 600000L); // 예제를 단순화하기 위해 'role'을 직접 지정했습니다.
        String refreshToken = jwtTokenProvider.createJwt("refresh", username, "ROLE_USER", 86400000L);
        refreshTokenRepository.save(new RefreshToken(username, refreshToken,accessToken));

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication Failed: " + failed.getMessage());
    }
}
