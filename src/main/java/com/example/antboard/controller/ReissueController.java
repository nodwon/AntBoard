package com.example.antboard.controller;

import com.example.antboard.Security.jwt.JwtTokenProvider;
import com.example.antboard.common.ErrorException;
import com.example.antboard.dto.response.member.JwtResponseDTO;
import com.example.antboard.entity.JwtToken;
import com.example.antboard.repository.JwtTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@ResponseBody
public class ReissueController {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

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

        JwtToken jwtToken = jwtTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new ErrorException("NOT_EXIST_REFRESH_JWT"));

//        try {
//            jwtTokenProvider.validateToken(jwtToken.getRefreshToken());
//        } catch (ExpiredJwtException e) {
//            return new ResponseEntity<>("Refresh token expired", HttpStatus.UNAUTHORIZED);
//        }

        String username = jwtTokenProvider.getUsername(jwtToken.getRefreshToken());
        String role = jwtTokenProvider.getRole(jwtToken.getRefreshToken());

        String newAccessToken = jwtTokenProvider.createJwt("access", username, role, 600000L); // 10 minutes
        String newRefreshToken = jwtTokenProvider.createJwt("refresh", username, role, 86400000L); // 24 hours

        jwtToken.setRefreshToken(newRefreshToken);
        jwtTokenRepository.save(jwtToken);

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(newAccessToken, newRefreshToken);

        Cookie newRefreshTokenCookie = createCookie(newRefreshToken);
        response.addCookie(newRefreshTokenCookie);

        return ResponseEntity.ok(jwtResponseDTO);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        cookie.setPath("/");
        return cookie;
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    String token = cookie.getValue();

                }
            }
        }
        return ResponseEntity.status(401).build();
    }

}
