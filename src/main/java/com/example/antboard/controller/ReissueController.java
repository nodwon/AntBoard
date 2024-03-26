package com.example.antboard.controller;

import com.example.antboard.Security.jwt.JwtTokenProvider;
import com.example.antboard.common.ErrorException;
import com.example.antboard.dto.response.member.TokenDto;
import com.example.antboard.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;


@Controller
@RequiredArgsConstructor
@ResponseBody
public class ReissueController {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshRepository refreshRepository;
    private final RedisTemplate<String, String> redisTemplate;


    @PostMapping({"/reissue"})
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(refresh);
        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (!Objects.requireNonNull(redisRefreshToken).equals(refresh)) {
            throw new ErrorException("NOT_EXIST_REFRESH_JWT");
        }
        try {
            this.jwtTokenProvider.isExpired(refresh);
        } catch (ExpiredJwtException var11) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = this.jwtTokenProvider.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        } else {
            Boolean isExist = this.refreshRepository.existsByRefresh(refresh);
            if (!isExist) {
                return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
            } else {
                String email = this.jwtTokenProvider.getUsername(refresh);
                String role = this.jwtTokenProvider.getRole(refresh);
                String newAccess = this.jwtTokenProvider.createJwt("access", email, role, 600000L);
                String newRefresh = this.jwtTokenProvider.createJwt("refresh", email, role, 86400000L);

                new TokenDto(newAccess, newRefresh);
                response.setHeader("access", newAccess);
                response.addCookie(this.createCookie(newRefresh));
                return new ResponseEntity<TokenDto>(HttpStatus.OK);
            }
        }
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(86400);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
