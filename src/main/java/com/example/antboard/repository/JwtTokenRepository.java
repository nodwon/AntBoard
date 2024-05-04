package com.example.antboard.repository;

import com.example.antboard.entity.JwtToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {

    Optional<JwtToken> findByAccessToken(@Param("Refresh") String accessToken);

    Optional<JwtToken> findByRefreshToken(@Param("RefreshToken")String refreshToeken);
}
