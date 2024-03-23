package com.example.antboard.controller;

import com.example.antboard.Security.jwt.JwtUtil;
import com.example.antboard.entity.RefreshEntity;
import com.example.antboard.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;


@Controller
@RequiredArgsConstructor
@ResponseBody
public class ReissueController {
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

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

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        } else {
            try {
                this.jwtUtil.isExpired(refresh);
            } catch (ExpiredJwtException var11) {
                return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
            }

            String category = this.jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
            } else {
                Boolean isExist = this.refreshRepository.existsByRefresh(refresh);
                if (!isExist) {
                    return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
                } else {
                    String email = this.jwtUtil.getUsername(refresh);
                    String role = this.jwtUtil.getRole(refresh);
                    String newAccess = this.jwtUtil.createJwt("access", email, role, 600000L);
                    String newRefresh = this.jwtUtil.createJwt("refresh", email, role, 86400000L);
                    this.refreshRepository.deleteByRefresh(refresh);
                    this.addRefreshEntity(email, newRefresh);
                    response.setHeader("access", newAccess);
                    response.addCookie(this.createCookie(newRefresh));
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
    }

    private void addRefreshEntity(String email, String refresh) {
        Date date = new Date(System.currentTimeMillis() + 86400000L);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        this.refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(86400);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
