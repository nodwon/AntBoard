package com.example.antboard.Security.jwt;

import com.example.antboard.entity.JwtToken;
import com.example.antboard.repository.JwtTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

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

        String token = null;
        String username = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                }
            }
        }
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        username = jwtTokenProvider.extractUsername(token);

        if (username != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
//        Optional<JwtToken> tokenOptional = jwtTokenRepository.findByAccessToken(token);
//        if (tokenOptional.isEmpty()) {
//            log.warn("Token not found in repository");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }

        filterChain.doFilter(request, response);
    }
}