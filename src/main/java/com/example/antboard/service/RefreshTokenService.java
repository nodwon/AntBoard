package com.example.antboard.service;

import com.example.antboard.entity.JwtToken;
import com.example.antboard.repository.JwtTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtTokenRepository jwtTokenRepository;

    @Transactional
    public void saveTokenInfo(Long id, String refreshToken, String accessToken) {
        jwtTokenRepository.save(new JwtToken(String.valueOf(id), refreshToken, accessToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        jwtTokenRepository.findByAccessToken(accessToken)
                .ifPresent(jwtTokenRepository::delete);
    }
}
