package com.example.antboard.Security.jwt;

import com.example.antboard.dto.response.member.MemberTokenDto;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.jwt.header}")
    private String HEADER_STRING;
    @Value("${spring.jwt.prefix}")
    private String TOKEN_PREFIX;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HEADER_STRING);
        String accessToken;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            accessToken = header.substring(TOKEN_PREFIX.length()).trim();

            try {
                if (jwtTokenProvider.validateToken(accessToken)) {
                    String email = jwtTokenProvider.getUsername(accessToken);
                    String role = jwtTokenProvider.getRole(accessToken);

                    MemberTokenDto memberTokenDto = new MemberTokenDto(email, role);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(memberTokenDto, null, memberTokenDto.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                log.info("Expired JWT token.", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token.");
                return;
            } catch (MalformedJwtException e) {
                log.info("Invalid JWT token.", e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token.");
                return;
            } catch (Exception e) {
                log.info("JWT processing failed.", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT processing failed.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}