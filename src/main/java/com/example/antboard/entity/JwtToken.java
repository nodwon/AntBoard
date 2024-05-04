package com.example.antboard.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@Setter
@RedisHash(value = "Refresh", timeToLive = 60*60*24*3)
public class JwtToken {

    @Id
    private String id;

    private String refreshToken;

    @Indexed
    private String accessToken;
}
