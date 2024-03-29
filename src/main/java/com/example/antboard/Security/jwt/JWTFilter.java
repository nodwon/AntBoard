package com.example.antboard.Security.jwt;

import com.example.antboard.dto.request.member.CustomMemberDetails;
import com.example.antboard.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${spring.jwt.header}")
    private String HEADER_STRING;
    //     private String HEADER_STRING = "Authorization";
    @Value("${spring.jwt.prefix}")
    private String TOKEN_PREFIX;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Thread currentThread = Thread.currentThread();
        log.info("현재 실행 중인 스레드: " + currentThread.getName());
        // get token
        String accessToken = request.getHeader(HEADER_STRING);

        if (accessToken == null) {
            filterChain.doFilter(request, response);

        } else {
            String category = this.jwtUtil.getCategory(accessToken);
            if (!category.equals("access")) {
                log.info("invalid access token");
                response.setStatus(401);
            } else {
                String email = jwtUtil.getUsername(accessToken);
                String role = this.jwtUtil.getRole(accessToken);
                Member member = Member.from(email);

                CustomMemberDetails customMemberDetails = new CustomMemberDetails(member, role);
                Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }

//        filterChain.doFilter(request, response);
        }
    }
}


