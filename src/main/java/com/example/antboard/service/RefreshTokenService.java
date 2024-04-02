package com.example.antboard.service;

import com.example.antboard.entity.RefreshToken;
import com.example.antboard.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(Long id, String refreshToken, String accessToken) {
        refreshTokenRepository.save(new RefreshToken(String.valueOf(id), refreshToken, accessToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}
