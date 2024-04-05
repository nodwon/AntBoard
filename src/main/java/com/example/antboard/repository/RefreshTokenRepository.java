package com.example.antboard.repository;

import com.example.antboard.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

    Optional<RefreshToken> findByRefreshToken(String refreshToeken);
}
