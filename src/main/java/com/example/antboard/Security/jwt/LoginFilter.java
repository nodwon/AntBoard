package com.example.antboard.Security.jwt;

import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.entity.RefreshToken;
import com.example.antboard.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
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
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(RefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        // 지정된 로그인 처리 URL 설정
        setFilterProcessesUrl("/user/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto =null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            ServletInputStream inputStream = request.getInputStream();
//            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password,new ArrayList<>());
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = (GrantedAuthority)iterator.next();
        String role = auth.getAuthority();
        String accessToken = jwtTokenProvider.createJwt("access", username, role, 600000L); // 예제를 단순화하기 위해 'role'을 직접 지정했습니다.
        String refreshToken = jwtTokenProvider.createJwt("refresh", username, role, 86400000L);
        refreshTokenRepository.save(new RefreshToken(username, refreshToken, accessToken));
        // 리프레시 토큰을 쿠키에 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setMaxAge(10 * 60); // 10분
        accessTokenCookie.setHttpOnly(true); // JavaScript 접근 방지
        accessTokenCookie.setPath("/"); // 전체 도메인 유효

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);

        log.info(String.valueOf(accessTokenCookie));

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication Failed: " + failed.getMessage());
    }
}
