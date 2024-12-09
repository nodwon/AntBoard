package com.example.antboard.Security.jwt;

import com.example.antboard.repository.JwtTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    private final JwtTokenRepository jwtTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String path = request.getRequestURI();

        // 한투 API 요청은 JWT 인증 제외
        if (path.startsWith("/api/balance")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String HEADER_STRING = "Authorization";
            String header = request.getHeader(HEADER_STRING);
            log.info("Received header: {}", header);
            String TOKEN_PREFIX = "Bearer ";
            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                String accessToken = header.substring(TOKEN_PREFIX.length()).trim();
                log.info("Extracted Access Token: {}", accessToken);  // Log the token for debugging
//                Optional<JwtToken> tokenOptional = jwtTokenRepository.findByAccessToken(accessToken);
//                if (tokenOptional.isEmpty()) {
//                    log.warn("Token not found in repository");
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    return;
//                }

                if (!jwtTokenProvider.validateToken(accessToken)) {
                    log.warn("Token validation failed");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String email = jwtTokenProvider.getUsername(accessToken);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                System.out.println(userDetails);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token.");
        } catch (Exception e) {
            log.info("JWT processing failed.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT processing failed.");
        }
    }
}