package com.example.usertemplate.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 30)
@Getter
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String refreshToken;

    private String email;
    private LocalDateTime validTime;
}