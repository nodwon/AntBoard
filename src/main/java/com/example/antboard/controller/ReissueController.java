package com.example.antboard.controller;

import com.example.antboard.Security.jwt.JwtTokenProvider;
import com.example.antboard.common.ErrorException;
import com.example.antboard.dto.response.member.TokenDto;
import com.example.antboard.entity.RefreshToken;
import com.example.antboard.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class ReissueController {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenValue = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenValue = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshTokenValue == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new ErrorException("NOT_EXIST_REFRESH_JWT"));

        try {
            jwtTokenProvider.validateToken(refreshToken.getRefreshToken());
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Refresh token expired", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtTokenProvider.getUsername(refreshToken.getRefreshToken());
        String role = jwtTokenProvider.getRole(refreshToken.getRefreshToken());

        String newAccessToken = jwtTokenProvider.createJwt("access", username, role, 600000L); // 10 minutes
        String newRefreshToken = jwtTokenProvider.createJwt("refresh", username, role, 86400000L); // 24 hours

        refreshToken.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        TokenDto tokenDto = new TokenDto(newAccessToken, newRefreshToken);

        Cookie newRefreshTokenCookie = createCookie(newRefreshToken);
        response.addCookie(newRefreshTokenCookie);

        return ResponseEntity.ok(tokenDto);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        cookie.setPath("/");
        return cookie;
    }
}
