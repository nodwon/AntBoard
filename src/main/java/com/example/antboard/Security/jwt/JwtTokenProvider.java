package com.example.antboard.Security.jwt;

import com.example.antboard.common.ErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//0.12.3
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret, CustomUserDetailsService customUserDetailsService) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.customUserDetailsService = customUserDetailsService;
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String category, String username, String role, Long expiredMs) {
        return Jwts.builder().claim("category", category).claim("username", username).claim("role", role).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(this.secretKey).compact();
    }

    public Authentication getAuthentication(String token) throws ErrorException {
        String MemberPrincipal = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(MemberPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new ErrorException("만료된 Access 토큰입니다. Refresh 토큰을 이용해서 새로운 Access 토큰을 발급 받으세요.");
        } catch (JwtException e) {
            throw new ErrorException("false, 2003, 지원되지 않거나 잘못된 토큰 입니다.");
        }
    }

    public String createJwt(String token, Authentication authentication) {
        Claims claims = (Claims) Jwts.claims().subject(authentication.getName());
        return Jwts.builder().claim(token, claims).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis())).signWith(this.secretKey).compact();

    }
}
