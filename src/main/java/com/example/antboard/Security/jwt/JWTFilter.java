package com.example.antboard.Security.jwt;

import com.example.antboard.common.Role;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtUtil;

    @Value("${spring.jwt.header}") private String HEADER_STRING;
    @Value("${spring.jwt.prefix}") private String TOKEN_PREFIX;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {

        Thread currentThread = Thread.currentThread();
        log.info("현재 실행 중인 스레드: " + currentThread.getName());

        // get token
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
//        String role = jwtUtil.getRole(String.valueOf(Role.ADMIN));

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX," ");
            try {
                username = this.jwtUtil.getUsername(authToken);
            } catch (IllegalArgumentException ex) {
                log.info("fail get user id");
                ex.printStackTrace();
            } catch (ExpiredJwtException ex) {
                log.info("Token expired");
                ex.printStackTrace();
            } catch (MalformedJwtException ex) {
                log.info("Invalid JWT !!");
                System.out.println();
                ex.printStackTrace();
            } catch (Exception e) {
                log.info("Unable to get JWT Token !!");
                e.getStackTrace();
            }

        } else {
            log.info("JWT does not begin with Bearer !!");
        }

        if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
            //log.info(SecurityContextHolder.getContext().getAuthentication().getName());
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.jwtUtil.validateToken(authToken, userDetails)) {

                // All things going well
                // Authentication stuff
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } else {
                log.info("Invalid JWT Token !!");
            }
        } else {
            log.info("Username is null or context is not null !!");
        }
        filterChain.doFilter(request, response);
    }
}


