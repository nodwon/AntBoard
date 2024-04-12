package com.example.antboard.Security.jwt;

import com.example.antboard.repository.JwtTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
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


    private final String HEADER_STRING = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenRepository jwtTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        try {
            String header = request.getHeader(HEADER_STRING);
            log.info("Received header: {}", header);
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
                System.out.println(email);
//                String role = jwtTokenProvider.getRole(accessToken);
//                Member member = new Member();
//                member.setUsername(email);
//                member.setRole(Role.valueOf(role));

//                User principal = new User(email, "", Collections.singletonList(new SimpleGrantedAuthority(role)));
//                CustomMemberDetails memberDetails = new CustomMemberDetails(member);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

//                Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
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