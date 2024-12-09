package com.example.antboard.Security.jwt;

import com.example.antboard.common.ErrorException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//0.12.3
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret, CustomUserDetailsService customUserDetailsService) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
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

    public String createJwt(String category, String email, String role, Long expiredMs) {
        return Jwts.builder().claim("category", category).
                claim("email", email).claim("role", role).
                issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(this.secretKey).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new ErrorException("만료된 Access 토큰입니다. Refresh 토큰을 이용해서 새로운 Access 토큰을 발급 받으세요.");
        } catch (UnsupportedJwtException e) {
            throw new ErrorException("false 2002지원되지 않는 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new ErrorException("false2003,잘못된 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new ErrorException("false 2005토큰이 비어있습니다.");
        }
    }

}
