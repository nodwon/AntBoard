package com.example.antboard.Security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^//logout$")) {
            filterChain.doFilter(request, response);
        } else {
            String requestMethod = request.getMethod();
            if (!requestMethod.equals("POST")) {
                filterChain.doFilter(request, response);
            } else {
                String refresh = null;
                Cookie[] cookies = request.getCookies();

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refresh")) {
                        refresh = cookie.getValue();
                    }
                }

                if (refresh == null) {
                    response.setStatus(400);
                } else {
                    try {
                        this.jwtTokenProvider.isExpired(refresh);
                    } catch (ExpiredJwtException var12) {
                        response.setStatus(400);
                        return;
                    }

                    String category = this.jwtTokenProvider.getCategory(refresh);
                    if (!category.equals("refresh")) {
                        response.setStatus(400);
                    } else {

                        Cookie cookie = new Cookie("refresh", null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        response.setStatus(200);
                    }
                }
            }
        }
    }
}
