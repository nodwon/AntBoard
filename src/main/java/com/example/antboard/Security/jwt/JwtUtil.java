package com.example.antboard.Security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
//0.12.3
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    public String getUsername(String token) {
        return (String)((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).get("email", String.class);
    }

    public String getRole(String token) {
        return (String)((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).get("role", String.class);
    }

    public String getCategory(String token) {
        return (String)((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).get("category", String.class);
    }

    public Boolean isExpired(String token) {
        return ((Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload()).getExpiration().before(new Date());
    }

    public String createJwt(String category, String email, String role, Long expiredMs) {
        return Jwts.builder().claim("category", category).claim("email", email).claim("role", role).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(this.secretKey).compact();
    }
}
